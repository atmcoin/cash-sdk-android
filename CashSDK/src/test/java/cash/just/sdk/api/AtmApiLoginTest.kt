package cash.just.sdk.api

import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import com.github.tomakehurst.wiremock.client.WireMock
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AtmApiLoginTest() : AbstractAtmApiTest() {


    @Test
    fun RestAtmLoginSuccess() {
        System.out.println("Start : RestAtmLoginSuccess" )
        InitSession()
    }

    //@Test
    fun RestAtmSessionCreationError502() {
        System.out.println("Start : RestAtmSessionCreationError502" )

        WireMock.stubFor(
            WireMock.post(WireMock.urlEqualTo("/atm/wac/guest/login"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(502)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("")
                )
        )
        val server = Cash.BtcNetwork.TEST_LOCAL
        var sessionKeyCreated: String = "";
        var error: String = "";
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.createSession(server, object : Cash.SessionCallback {
            override fun onSessionCreated(sessionKey: String) {
                sessionKeyCreated = sessionKey;
                countDownResponse.countDown();
            }

            override fun onError(errorMessage: String?) {
                error = "error :" + errorMessage
                countDownResponse.countDown();
            }
        })
        countDownResponse.await(500,TimeUnit.MILLISECONDS)
        System.out.println("sessionKey : " + sessionKeyCreated)
        System.out.println("Error : " + error)
        Assert.assertTrue("Error Message Empty " + error, StringUtils.isNotBlank(error));
        Assert.assertFalse("Session  created ", CashSDK.isSessionCreated());
        Assert.assertTrue("Session key empty", StringUtils.isBlank(sessionKeyCreated));



    }

    //@Test
    fun RestAtmS0essionCreationErrorFormat() {
        System.out.println("Start : RestAtmS0essionCreationErrorFormat" )

        WireMock.stubFor(
            WireMock.post(WireMock.urlEqualTo("/atm/wac/guest/login"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"result\": \"ok\",\"error\": null,\"data\": {\"Key\": \"" + SESSION_KEY + "\"}}")
                )
        )
        val server = Cash.BtcNetwork.TEST_LOCAL
        var sessionKeyCreated: String = "";
        var error: String = "";
        val countDownResponse : CountDownLatch = CountDownLatch(1)
        CashSDK.createSession(server, object : Cash.SessionCallback {
            override fun onSessionCreated(sessionKey: String) {
                sessionKeyCreated = sessionKey;
                countDownResponse.countDown();
            }

            override fun onError(errorMessage: String?) {
                error = "error :" + errorMessage
                countDownResponse.countDown();
            }
        })
        countDownResponse.await()
        System.out.println("sessionKey : " + sessionKeyCreated)
        System.out.println("Error : " + error)
        Assert.assertTrue("Session key Error " + error, StringUtils.isNotBlank(error));
        Assert.assertFalse("Session  created ", CashSDK.isSessionCreated());
        Assert.assertTrue("Session key empty", StringUtils.isBlank(sessionKeyCreated));
    }

}