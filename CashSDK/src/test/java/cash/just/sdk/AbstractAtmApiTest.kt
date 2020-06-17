package cash.just.sdk

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
abstract class AbstractAtmApiTest {

    public var SESSION_KEY :String= "1111"
    public var LOGIN_RESPONSE :String="{\"result\": \"ok\",\"error\": null,\"data\": {\"sessionKey\": \"" + SESSION_KEY + "\"}}"
    @Rule
    @JvmField
    var wireMockRule: WireMockRule = WireMockRule(WireMockConfiguration().notifier(ConsoleNotifier(true)).port(8080))




    fun InitSession() {

        stubFor(
            post(urlEqualTo("/atm/wac/guest/login"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(LOGIN_RESPONSE)
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
        Assert.assertTrue("Session key Error " + error, StringUtils.isBlank(error));
        Assert.assertTrue("Session key empty", StringUtils.isNotBlank(sessionKeyCreated));

        Assert.assertTrue("Session  created "  , CashSDK.isSessionCreated());
    }
}
