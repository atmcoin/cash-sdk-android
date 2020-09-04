package cash.just.sdk.api

import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Rule
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
abstract class AbstractAtmApiTest {

    var SESSION_KEY = "session234sdfsdfjhl"
    var LOGIN_RESPONSE  =
        "{\"result\": \"ok\",\"error\": null,\"data\": {\"sessionKey\": \"$SESSION_KEY\"}}"
    @Rule
    @JvmField
    var wireMockRule: WireMockRule = WireMockRule(WireMockConfiguration().notifier(ConsoleNotifier(true)).port(8080))

    fun initSession() {
        stubFor(
            post(urlEqualTo("/wac/wac/guest/login"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(LOGIN_RESPONSE)
                )
        )
        val server = Cash.BtcNetwork.TEST_LOCAL
        var sessionKeyCreated = ""
        var error = ""
        val countDownResponse = CountDownLatch(1)
        CashSDK.createGuestSession(server, object : Cash.SessionCallback {
            override fun onSessionCreated(sessionKey: String) {
                sessionKeyCreated = sessionKey
                countDownResponse.countDown()
            }

            override fun onError(errorMessage: String?) {
                error = "error :$errorMessage"
                countDownResponse.countDown()
            }
        })
        countDownResponse.await()
        println("sessionKey : $sessionKeyCreated")
        Assert.assertTrue("Session key Error $error", StringUtils.isBlank(error))
        Assert.assertTrue("Session key empty", StringUtils.isNotBlank(sessionKeyCreated))
        Assert.assertTrue("Session  created "  , CashSDK.isSessionCreated())
    }
}
