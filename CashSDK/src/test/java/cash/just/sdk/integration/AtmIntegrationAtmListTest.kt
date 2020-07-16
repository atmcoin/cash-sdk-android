package cash.just.sdk.integration

import cash.just.sdk.Cash
import cash.just.sdk.CashSDK
import cash.just.sdk.model.AtmListResponse
import cash.just.sdk.model.AtmMachine
import com.github.tomakehurst.wiremock.client.WireMock
import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AtmIntegrationAtmListTest : AbstractAtmIntegrationTest() {


    @Test
    fun restAtmListSuccess() {
        println("Start : RestAtmListSuccess")
        initSession()


        var error: String? = ""
        lateinit var atms: Response<AtmListResponse>
        val countDownResponse = CountDownLatch(1)
        CashSDK.getAtmList().enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown()
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                atms = response;
                countDownResponse.countDown()
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session  created ", atms)
        Assert.assertTrue("isSuccessful$error", atms.isSuccessful)
        Assert.assertTrue("Error  Empty $error", StringUtils.isBlank(error))

        Assert.assertTrue("List item size >0", atms.body()!!.data.items.size > 0)

        val atmMachine: AtmMachine = atms.body()!!.data.items[0]
        System.out.println("atmMachine.atmId >> " + atmMachine.atmId)
        System.out.println("atmMachine.addressDesc >> " + atmMachine.addressDesc)
        System.out.println("atmMachine.bills >> " + atmMachine.bills)
        System.out.println("atmMachine.city >> " + atmMachine.city)
        System.out.println("atmMachine.currency >> " + atmMachine.currency)
        System.out.println("atmMachine.desc >> " + atmMachine.desc)
        System.out.println("atmMachine.detail >> " + atmMachine.detail)
        System.out.println("atmMachine.fees >> " + atmMachine.fees)
        System.out.println("atmMachine.latitude >> " + atmMachine.latitude)
        System.out.println("atmMachine.longitude >> " + atmMachine.longitude)
        System.out.println("atmMachine.max >> " + atmMachine.max)
        System.out.println("atmMachine.min >> " + atmMachine.min)
        System.out.println("atmMachine.redemption >> " + atmMachine.redemption)
        System.out.println("atmMachine.zip >> " + atmMachine.zip)

        Assert.assertTrue("atmid empty", StringUtils.isNumeric(atmMachine.atmId));
        Assert.assertTrue("address desc", StringUtils.isNotBlank(atmMachine.addressDesc))
        Assert.assertTrue("bills", decimalPattern.matcher(atmMachine.bills).matches())
        Assert.assertTrue("city", StringUtils.isNotBlank(atmMachine.city))
        Assert.assertEquals("USD", atmMachine.currency)
        Assert.assertTrue("atm desc", StringUtils.isNotBlank(atmMachine.desc))
        //Assert.assertEquals("detail", "address detail",atmMachine.detail)
        //Assert.assertEquals("fees", null,atmMachine.fees)
        Assert.assertTrue("latitude ", decimalPattern.matcher(atmMachine.latitude).matches())
        Assert.assertTrue("longitude ", decimalPattern.matcher(atmMachine.longitude).matches())
        Assert.assertTrue("max", decimalPattern.matcher(atmMachine.max).matches())
        Assert.assertTrue("min", decimalPattern.matcher(atmMachine.min).matches())
        Assert.assertEquals("redemption", 0, atmMachine.redemption)
        Assert.assertTrue("zip", StringUtils.isNumeric(atmMachine.zip))
    }


    //@Test
    fun restAtmListNearSuccess() {
        println("Start : RestAtmListNearSuccess")
        initSession()


        var error: String? = ""
        lateinit var atms: Response<AtmListResponse>
        val countDownResponse = CountDownLatch(1)
        CashSDK.getAtmListByLocation("28.2179330", "-82.4507400").enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown()
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                atms = response
                countDownResponse.countDown()
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session  created ", atms);
        Assert.assertTrue("isSuccessful$error", atms.isSuccessful)
        Assert.assertTrue("Error  Empty $error", StringUtils.isBlank(error))

        Assert.assertTrue("List item size >0", atms.body()!!.data.items.size > 0)

        val atmMachine: AtmMachine = atms.body()!!.data.items[0]
        System.out.println("atmMachine.atmId >> " + atmMachine.atmId)
        System.out.println("atmMachine.addressDesc >> " + atmMachine.addressDesc)
        System.out.println("atmMachine.bills >> " + atmMachine.bills)
        System.out.println("atmMachine.city >> " + atmMachine.city)
        System.out.println("atmMachine.currency >> " + atmMachine.currency)
        System.out.println("atmMachine.desc >> " + atmMachine.desc)
        System.out.println("atmMachine.detail >> " + atmMachine.detail)
        System.out.println("atmMachine.fees >> " + atmMachine.fees)
        System.out.println("atmMachine.latitude >> " + atmMachine.latitude)
        System.out.println("atmMachine.longitude >> " + atmMachine.longitude)
        System.out.println("atmMachine.max >> " + atmMachine.max)
        System.out.println("atmMachine.min >> " + atmMachine.min)
        System.out.println("atmMachine.redemption >> " + atmMachine.redemption)
        System.out.println("atmMachine.zip >> " + atmMachine.zip)

        Assert.assertTrue("atmid empty", StringUtils.isNumeric(atmMachine.atmId));
        Assert.assertTrue("address desc", StringUtils.isNotBlank(atmMachine.addressDesc))
        Assert.assertTrue("bills", decimalPattern.matcher(atmMachine.bills).matches())
        Assert.assertTrue("city", StringUtils.isNotBlank(atmMachine.city))
        Assert.assertEquals("USD", atmMachine.currency)
        Assert.assertTrue("atm desc", StringUtils.isNotBlank(atmMachine.desc))
        //Assert.assertEquals("detail", "address detail",atmMachine.detail)
        //Assert.assertEquals("fees", null,atmMachine.fees)
        Assert.assertTrue("latitude ", decimalPattern.matcher(atmMachine.latitude).matches())
        Assert.assertTrue("longitude ", decimalPattern.matcher(atmMachine.longitude).matches())
        Assert.assertTrue("max", decimalPattern.matcher(atmMachine.max).matches())
        Assert.assertTrue("min", decimalPattern.matcher(atmMachine.min).matches())
        Assert.assertEquals("redemption", 0, atmMachine.redemption)
        Assert.assertTrue("zip", StringUtils.isNumeric(atmMachine.zip))
    }

}
