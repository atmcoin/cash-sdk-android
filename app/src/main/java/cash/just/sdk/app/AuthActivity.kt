package cash.just.sdk.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.sdk.model.*
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        loginButton.setOnClickListener {
            CashSDK.login(getServer(), userPhoneNumber.text.toString(), object: Cash.WacCallback {
                override fun onSucceed() {
                    Toast.makeText(applicationContext, "on succeed", Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String?) {
                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

        confirmButton.setOnClickListener {
            CashSDK.loginConfirm(confirmLogin.text.toString(), object: Cash.WacCallback {
                override fun onSucceed() {
                    Toast.makeText(applicationContext, "on succeed", Toast.LENGTH_SHORT).show()
                }

                override fun onError(errorMessage: String?) {
                    Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

        kycButton.setOnClickListener {
            CashSDK.getKycStatus().enqueue(object: Callback<KycStatusResponse> {
                override fun onResponse(call: Call<KycStatusResponse>, response: Response<KycStatusResponse>) {
                    if(response.isSuccessful)
                        Toast.makeText(applicationContext, response.body()?.data?.items?.get(0)?.status.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<KycStatusResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        registerButton.setOnClickListener {
            val server = getServer()
            CashSDK.register(server,
                registerPhoneNumber.text.toString(),
                registerName.text.toString(),
                registerSurname.text.toString(), object:Cash.WacCallback {
                    override fun onSucceed() {
                        Toast.makeText(applicationContext, "registered", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(errorMessage: String?) {
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
