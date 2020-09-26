package cash.just.sdk.integration

import cash.just.sdk.CashSDK
import cash.just.sdk.model.CashCodeResponse
import cash.just.sdk.model.CashCodeStatusResponse
import cash.just.sdk.model.SendVerificationCodeResponse
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AtmIntegrationCashCodeTest : AbstractAtmIntegrationTest() {


    @Test
    fun restVerificationCodeSuccess() {
        println("Start : RestVerificationCodeSuccess" )
        initSession()



        var error: String? = ""
        lateinit  var verificationCodeResponse: Response<SendVerificationCodeResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.sendVerificationCode("first","last","00001111","email@atm").enqueue(object : retrofit2.Callback<SendVerificationCodeResponse> {
            override fun onFailure(call: Call<SendVerificationCodeResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<SendVerificationCodeResponse>, response: Response<SendVerificationCodeResponse>) {
                verificationCodeResponse = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session  created ", verificationCodeResponse)
        Assert.assertTrue("isSuccessful$error", verificationCodeResponse.isSuccessful)
        Assert.assertTrue("Error Empty $error", StringUtils.isBlank(error))
        println("verificationCodeResponse.body() : " + verificationCodeResponse.body())
        Assert.assertEquals("Result OK", "OK", verificationCodeResponse.body()!!.data.items[0].result)


    }

    @Test
    fun restCreateCodeError() {
        println("Start : RestCreateCodeError" )
        initSession()


        var error: String? = ""
        lateinit  var cashCodeResponse: Response<CashCodeResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.createCashCode("5013","2","22222").enqueue(object : retrofit2.Callback<CashCodeResponse> {
            override fun onFailure(call: Call<CashCodeResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown()
            }

            override fun onResponse(call: Call<CashCodeResponse>, response: Response<CashCodeResponse>) {
                cashCodeResponse = response
                countDownResponse.countDown()
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session  created ", cashCodeResponse)
        Assert.assertFalse("isSuccessful$error", cashCodeResponse.isSuccessful)
        Assert.assertTrue("Error Empty $error", StringUtils.isBlank(error))
        println("cashCodeResponse.body() : $cashCodeResponse")
        Assert.assertEquals("Result OK", "500", cashCodeResponse.message())

    }


    @Test
    fun restCheckCodeError() {
        println("Start : RestCheckCodeError" )
        initSession()

        var error: String? = "";
        lateinit  var cashCodeStatusResponse: Response<CashCodeStatusResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.checkCashCodeStatus("111").enqueue(object : retrofit2.Callback<CashCodeStatusResponse> {
            override fun onFailure(call: Call<CashCodeStatusResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown()
            }

            override fun onResponse(call: Call<CashCodeStatusResponse>, response: Response<CashCodeStatusResponse>) {
                cashCodeStatusResponse = response
                countDownResponse.countDown()
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session created ", cashCodeStatusResponse)
        Assert.assertFalse("isSuccessful$error", cashCodeStatusResponse.isSuccessful)
        Assert.assertTrue("Error Empty $error", StringUtils.isBlank(error))
        println("cashCodeResponse.body() : $cashCodeStatusResponse")
        Assert.assertEquals("Result OK", "500", cashCodeStatusResponse.message())
    }


}
