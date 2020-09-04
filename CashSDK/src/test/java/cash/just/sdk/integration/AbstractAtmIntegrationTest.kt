package cash.just.sdk.integration

import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.sdk.IntegrationTest
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.experimental.categories.Category
import java.util.concurrent.CountDownLatch
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Category(IntegrationTest::class)
abstract class AbstractAtmIntegrationTest {


    var decimalPattern:Pattern = Pattern.compile("\\-?\\d+\\.\\d+");

    fun initSession() {

        val server = Cash.BtcNetwork.TEST_NET
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
        Assert.assertTrue("Session  created ", CashSDK.isSessionCreated())
    }
}
