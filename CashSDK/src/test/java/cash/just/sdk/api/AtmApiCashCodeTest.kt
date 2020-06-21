package cash.just.sdk.api

import android.widget.Toast
import cash.just.sdk.CashSDK
import cash.just.sdk.model.*
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
    var RESPONSE_CODE_CREATE_SUCCESS ="{ \"result\": \"ok\", \"error\": null, \"data\": { \"items\": [ { \"secure_code\": \"secure1111\", \"address\": \"address1111\", \"usd_amount\": \"20\", \"btc_amount\": \"0.002\", \"btc_whole_unit_price\": \"8122\" } ] }, \"metrics\": { \"data_time\": 0, \"data_size\": 0 }}"
    var RESPONSE_CODE_CREATE_ERROR ="{\"result\": \"error\",\"error\": {\"code\": \"\",\"server_message\": \"Wrong amount!\"},\"data\": null}"
    var RESPONSE_CODE_CHECK_SUCCESS ="{\"result\": \"ok\",\"error\": null,\"data\": {\"items\": [{\"pcode\": \"pcode\",\"status\": \"A\",\"address\": \"address1111\",\"usd_amount\": \"20\",\"btc_amount\": \"0.002\",\"btc_whole_unit_price\": \"8122\",\"expiration\": \"2020-06-00T00:00:00Z\",\"atm_id\": \"8078\",\"loc_description\": \"loc description\",\"loc_lat\": \"30.00\",\"loc_lon\": \"-30.00\"}]},\"metrics\": {\"data_time\": 0,\"data_size\": 0}}"
    var RESPONSE_CODE_CHECK_ERROR="{\"result\": \"error\",\"error\": {\"code\": \"\",\"server_message\": \"Purchase code not found!\"},\"data\": null}"

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


    @Test
    fun RestCreateCodeError() {
        System.out.println("Start : RestCreateCodeError" )
        InitSession()

        stubFor(
            post(urlPathEqualTo("/atm/wac/pcode"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .withQueryParam("atm_id",equalTo("1234"))
                .withQueryParam("amount",equalTo("2"))
                .withQueryParam("verification_code",equalTo("22222"))

                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_CODE_CREATE_ERROR)
                )
        )

        var error: String? = "";
        lateinit  var cashCodeResponse: Response<CashCodeResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.createCashCode("1234","2","22222").enqueue(object : retrofit2.Callback<CashCodeResponse> {
            override fun onFailure(call: Call<CashCodeResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<CashCodeResponse>, response: Response<CashCodeResponse>) {
                cashCodeResponse = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", cashCodeResponse);
        Assert.assertFalse("isSuccessful" + error, cashCodeResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("cashCodeResponse.body() : " + cashCodeResponse)
        Assert.assertEquals("Result OK", "Server Error",cashCodeResponse.message());

    }

    @Test
    fun RestCreateCodeSucces() {
        System.out.println("Start : RestCreateCodeSucces" )
        InitSession()

        stubFor(
            post(urlPathEqualTo("/atm/wac/pcode"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .withQueryParam("atm_id",equalTo("1234"))
                .withQueryParam("amount",equalTo("20"))
                .withQueryParam("verification_code",equalTo("22222"))

                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_CODE_CREATE_SUCCESS)
                )
        )

        var error: String? = "";
        lateinit  var cashCodeResponse: Response<CashCodeResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.createCashCode("1234","20","22222").enqueue(object : retrofit2.Callback<CashCodeResponse> {
            override fun onFailure(call: Call<CashCodeResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<CashCodeResponse>, response: Response<CashCodeResponse>) {
                cashCodeResponse = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", cashCodeResponse);
        Assert.assertTrue("isSuccessful" + error, cashCodeResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("verificationCodeResponse.body() : " + cashCodeResponse.body())
        Assert.assertEquals("Result OK", 1,cashCodeResponse.body()!!.data.items.size);
        var  cashCode:CashCode=cashCodeResponse.body()!!.data.items.get(0)
        Assert.assertEquals("btc_amount OK", "0.002",cashCode.btc_amount);
        Assert.assertEquals("secureCode OK", "secure1111",cashCode.secureCode);
        Assert.assertEquals("address OK", "address1111",cashCode.address);
        Assert.assertEquals("unitPrice OK", "8122",cashCode.unitPrice);
        Assert.assertEquals("usdAmount OK", "20",cashCode.usdAmount);

    }


    @Test
    fun RestCheckCodeError() {
        System.out.println("Start : RestCheckCodeError" )
        InitSession()

        stubFor(
            get(urlPathEqualTo("/atm/wac/pcode/111"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_CODE_CHECK_ERROR)
                )
        )

        var error: String? = "";
        lateinit  var cashCodeStatusResponse: Response<CashCodeStatusResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.checkCashCodeStatus("111").enqueue(object : retrofit2.Callback<CashCodeStatusResponse> {
            override fun onFailure(call: Call<CashCodeStatusResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<CashCodeStatusResponse>, response: Response<CashCodeStatusResponse>) {
                cashCodeStatusResponse = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", cashCodeStatusResponse);
        Assert.assertFalse("isSuccessful" + error, cashCodeStatusResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("cashCodeResponse.body() : " + cashCodeStatusResponse)
        Assert.assertEquals("Result OK", "Server Error",cashCodeStatusResponse.message());


    }


    @Test
    fun RestCheckCodeSuccess() {
        System.out.println("Start : RestCheckCodeSuccess" )
        InitSession()

        stubFor(
            get(urlPathEqualTo("/atm/wac/pcode/111"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_CODE_CHECK_SUCCESS)
                )
        )

        var error: String? = "";
        lateinit  var cashCodeStatusResponse: Response<CashCodeStatusResponse>
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.checkCashCodeStatus("111").enqueue(object : retrofit2.Callback<CashCodeStatusResponse> {
            override fun onFailure(call: Call<CashCodeStatusResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<CashCodeStatusResponse>, response: Response<CashCodeStatusResponse>) {
                cashCodeStatusResponse = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", cashCodeStatusResponse);
        Assert.assertTrue("isSuccessful" + error, cashCodeStatusResponse.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));
        System.out.println("cashCodeStatusResponse.body() : " + cashCodeStatusResponse.body())
        Assert.assertEquals("Result OK", 1,cashCodeStatusResponse.body()!!.data!!.items.size);
        var  cashStatus:CashStatus=cashCodeStatusResponse.body()!!.data!!.items.get(0)

        Assert.assertEquals("btc_amount OK", "8078",cashStatus.atmId);
        Assert.assertEquals("btc_amount OK", "0.002",cashStatus.btc_amount);
        Assert.assertEquals("code OK", "pcode",cashStatus.code);
        Assert.assertEquals("address OK", "address1111",cashStatus.address);
        Assert.assertEquals("unitPrice OK", "8122",cashStatus.unitPrice);
        Assert.assertEquals("usdAmount OK", "20",cashStatus.usdAmount);
        Assert.assertEquals("description OK", "loc description",cashStatus.description);
        Assert.assertEquals("expiration OK", "2020-06-00T00:00:00Z",cashStatus.expiration);
        Assert.assertEquals("longitude OK", "-30.00",cashStatus.longitude);
        Assert.assertEquals("latitude OK", "30.00",cashStatus.latitude);
    }
}
