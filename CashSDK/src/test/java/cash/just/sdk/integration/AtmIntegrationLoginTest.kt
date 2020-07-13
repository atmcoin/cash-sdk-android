package cash.just.sdk.integration

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
class AtmIntegrationLoginTest : AbstractAtmIntegrationTest() {

    @Test
    fun restAtmLoginSuccess() {
        println("Start : RestAtmLoginSuccess" )
        initSession()
    }

}
