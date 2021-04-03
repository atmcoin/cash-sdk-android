package cash.just.sdk.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.sdk.model.AtmListResponse
import cash.just.sdk.model.CashCodeResponse
import cash.just.sdk.model.CashCodeStatusResponse
import cash.just.sdk.model.SendVerificationCodeResponse
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        advancedLogin.visibility = View.GONE

        serverToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                (application as App).server = Cash.BtcNetwork.MAIN_NET
            } else {
                (application as App).server = Cash.BtcNetwork.TEST_NET
            }
        }

        afterLoginPanel.visibility = View.GONE

        advancedLogin.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        guestLoginButton.setOnClickListener {
            CashSDK.createGuestSession(getServer(), object: Cash.SessionCallback {
                override fun onSessionCreated(sessionKey: String) {
                    session.setText(sessionKey)
                    advancedLogin.visibility = View.VISIBLE
                    afterLoginPanel.visibility = View.VISIBLE
                }

                override fun onError(errorMessage: String?) {
                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

        getAtmList.setOnClickListener {
            list.text.clear()
            CashSDK.getAtmList().enqueue(object: retrofit2.Callback<AtmListResponse> {
                override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                    list.setText(response.body()!!.data.toString())
                }
            })
        }

        getAtmListByLatitude.setOnClickListener {
            list.text.clear()
            CashSDK.getAtmListByLocation(lat.text.toString(), lon.text.toString())
                .enqueue(object: retrofit2.Callback<AtmListResponse> {
                    override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                        list.setText(response.body()!!.data.toString())
                    }
            })
        }

        checkCode.setOnClickListener {
            CashSDK.checkCashCodeStatus(code.text.toString()).enqueue(object: retrofit2.Callback<CashCodeStatusResponse> {
                override fun onResponse(call: Call<CashCodeStatusResponse>, response: Response<CashCodeStatusResponse>) {
                    if (response.isSuccessful
                        && response.body() != null
                        && response.body()!!.data != null
                        && response.body()!!.data!!.items.isNotEmpty()
                    ) {
                        val result = response.body()!!.data!!.items[0]
                        Toast.makeText(applicationContext, result.getCodeStatus().toString() + " " + result.toString(), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CashCodeStatusResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        sendVerificationCode.setOnClickListener {
            CashSDK.sendVerificationCode(
                firstName.text.toString(),
                lastName.text.toString(),
                phoneNumber.text.toString(),
                email.text.toString()).enqueue(object: retrofit2.Callback<SendVerificationCodeResponse> {
                override fun onFailure(call: Call<SendVerificationCodeResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<SendVerificationCodeResponse>, responseVerification: Response<SendVerificationCodeResponse>) {
                    if (responseVerification.isSuccessful) {
                        Toast.makeText(applicationContext, responseVerification.body()!!.data.toString(), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, responseVerification.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        createCode.setOnClickListener {
            CashSDK.createCashCode(
                atmId.text.toString(),
                amount.text.toString(),
                verificationCode.text.toString()).enqueue(object: retrofit2.Callback<CashCodeResponse> {
                override fun onFailure(call: Call<CashCodeResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<CashCodeResponse>, response: Response<CashCodeResponse>) {
                    if (response.isSuccessful) {
                        val responseText = response.body()!!.data.toString()
                        createCodeResult.setText(responseText)
                        Toast.makeText(applicationContext, responseText, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        scanQrCode.setOnClickListener {
            val integrator = IntentIntegrator(this)

            integrator.setOrientationLocked(false)
            integrator.setPrompt("Scan QR code")
            integrator.setBeepEnabled(false) //Use this to set whether you need a beep sound when the QR code is scanned


            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE, IntentIntegrator.PDF_417)


            integrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                if(result.formatName == IntentIntegrator.PDF_417) {
                    val resultMaps: HashMap<String, String>? = DriverLicenseUtil.readDriverLicense(result.contents)
                    driverDetails.visibility =View.VISIBLE
                    driverDetails.text =
                        "Driver Name: ${resultMaps?.get(DriverLicenseUtil.FIRST_NAME)} ${resultMaps?.get(DriverLicenseUtil.LAST_NAME)} " +
                                "\nLicense Number: ${resultMaps?.get(DriverLicenseUtil.LICENSE_NUMBER)} " +
                                "\nAddress: ${resultMaps?.get(DriverLicenseUtil.STREET)}"
                }
                println(result.contents)
            }
        }
    }
}