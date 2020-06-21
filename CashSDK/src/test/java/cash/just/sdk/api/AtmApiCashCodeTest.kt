package cash.just.sdk.api

import android.widget.Toast
import cash.just.sdk.CashSDK
import cash.just.sdk.model.AtmListResponse
import cash.just.sdk.model.SendVerificationCodeResponse
import com.github.tomakehurst.wiremock.client.WireMock
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import retrofit2.Call
import retrofit2.Response
import com.github.tomakehurst.wiremock.client.WireMock.*
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AtmApiCashCodeTest() : AbstractAtmApiTest() {
    var RESPONSE_VERIFICATION_SUCCESS = "{ \"result\": \"ok\", \"error\": null, \"data\": { \"items\": [ { \"result\": \"OK\" } ] }, \"metrics\": { \"data_time\": 0, \"data_size\": 0 } }"
    var RESPONSE_VERIFICATION_ERROR = "{ \"result\": \"error\", \"error\": { \"code\": \"103\", \"server_message\": \"Wrong parameters\" }, \"data\": null}"


    @Test
    fun RestVerificationCodeError() {
        System.out.println("Start : RestVerificationCodeError" )
        InitSession()

        stubFor(
            post(urlPathEqualTo("/atm/wac/pcode/verify"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .withQueryParam("first_name",equalTo("first"))
                .withQueryParam("last_name",equalTo("last"))


                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_VERIFICATION_ERROR)
                )
        )

        var error: String? = "";
        lateinit  var verificationCodeResponse: Response<SendVerificationCodeResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.sendVerificationCode("first","last",null,null).enqueue(object : retrofit2.Callback<SendVerificationCodeResponse> {
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

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", verificationCodeResponse);
        Assert.assertFalse("isSuccessful" + error, verificationCodeResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("verificationCodeResponse.body() : " + verificationCodeResponse)
        Assert.assertEquals("Result OK", "Server Error",verificationCodeResponse.message());

    }


    @Test
    fun RestVerificationCodeSuccess() {
        System.out.println("Start : RestVerificationCodeSuccess" )
        InitSession()

        stubFor(
            post(urlPathEqualTo("/atm/wac/pcode/verify"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .withQueryParam("phone_number",equalTo("00001111"))
                .withQueryParam("email",equalTo("email@atm"))
                .withQueryParam("first_name",equalTo("first"))
                .withQueryParam("last_name",equalTo("last"))


                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_VERIFICATION_SUCCESS)
                )
        )

        var error: String? = "";
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

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", verificationCodeResponse);
        Assert.assertTrue("isSuccessful" + error, verificationCodeResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("verificationCodeResponse.body() : " + verificationCodeResponse.body())
        Assert.assertEquals("Result OK", "OK",verificationCodeResponse.body()!!.data.items.get(0).result);


    }

}
