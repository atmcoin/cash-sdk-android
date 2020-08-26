package cash.just.sdk.api

import cash.just.sdk.CashSDK
import cash.just.sdk.model.AtmListResponse
import cash.just.sdk.model.AtmMachine
import com.github.tomakehurst.wiremock.client.WireMock.*
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
class AtmApiAtmListTest : AbstractAtmApiTest() {
    var RESPONSE_LIST_ATMS =
        "{ \"result\": \"ok\", \"error\": null, \"data\": { \"items\": [ { \"atm_id\": \"5013\", \"address_desc\": \"address desc\", \"address_street\": \"Mast Court\", \"address_detail\": \"address detail\", \"address_city\": \"NY\", \"address_state\": \"FL\", \"address_zip\": \"123\", \"loc_lon\": \"-30.00\", \"loc_lat\": \"30.00\", \"atm_desc\": \"atm desc\", \"atm_min\": \"20.00\", \"atm_max\": \"500.00\", \"atm_bills\": \"20.00\", \"atm_currency\": \"USD\", \"atm_red\": \"0\" }, { \"atm_id\": \"5014\", \"address_desc\": \"4601 BASSWOOD ST, Land O Lakes\", \"address_street\": \"Grand Oaks Boulevard\", \"address_detail\": null, \"address_city\": \"Land O Lakes\", \"address_state\": \"FL\", \"address_zip\": \"34639\", \"loc_lon\": \"-82.3846080\", \"loc_lat\": \"28.2228480\", \"atm_desc\": \"4601 BASSWOOD ST\", \"atm_min\": \"20.00\", \"atm_max\": \"400.00\", \"atm_bills\": \"20.00\", \"atm_currency\": \"USD\", \"atm_red\": \"1\" } ] } }"

    @Test
    fun restAtmListSuccess() {
        println("Start : RestAtmListSuccess" )
        initSession()

        stubFor(
            get(urlEqualTo("/wac/wac/atm/list"))
                .withHeader("sessionKey", equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = ""
        lateinit  var atms: Response<AtmListResponse>
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

        Assert.assertEquals("List item size 2", 2, atms.body()!!.data.items.size)
        val atmMachine : AtmMachine = atms.body()!!.data.items[0]
        Assert.assertEquals("atmid 5013", "5013",atmMachine.atmId);
        Assert.assertEquals("address desc", "address desc",atmMachine.addressDesc)
        Assert.assertEquals("bills", "20.00",atmMachine.bills)
        Assert.assertEquals("city", "NY",atmMachine.city)
        Assert.assertEquals("currency", "USD",atmMachine.currency)
        Assert.assertEquals("atm desc", "atm desc",atmMachine.desc)
        Assert.assertEquals("detail", "address detail",atmMachine.detail)
        Assert.assertEquals("fees", null,atmMachine.fees)
        Assert.assertEquals("latitude ", "30.00",atmMachine.latitude)
        Assert.assertEquals("longitude ", "-30.00",atmMachine.longitude)
        Assert.assertEquals("max", "500.00",atmMachine.max)
        Assert.assertEquals("min", "20.00",atmMachine.min)
        Assert.assertEquals("redemption", 0,atmMachine.redemption)
        Assert.assertEquals("zip", "123",atmMachine.zip)
    }

    @Test
    fun restAtmListError() {
        println("Start : RestAtmListError" )
        initSession()

        stubFor(
            get(urlEqualTo("/wac/wac/atm/list"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(502)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = "";
        lateinit  var ATMs: Response<AtmListResponse>
        val countDownResponse = CountDownLatch(1)
        CashSDK.getAtmList().enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
                countDownResponse.countDown();
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                ATMs = response;
                countDownResponse.countDown();
            }
        })

        countDownResponse.await()

        println("Error : $error")
        Assert.assertNotNull("Session  created", ATMs)
        Assert.assertFalse("isSuccessful$error", ATMs.isSuccessful)
    }

    @Test
    fun restAtmListNearSuccess() {
        println("Start : RestAtmListNearSuccess" )
        initSession()

        stubFor(
            get(urlEqualTo("/wac/wac/atm/near/latlon/2/1"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = ""
        lateinit  var atms: Response<AtmListResponse>
        val countDownResponse = CountDownLatch(1)
        CashSDK.getAtmListByLocation("2","1").enqueue(object : retrofit2.Callback<AtmListResponse> {
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

        Assert.assertEquals("List item size 2", 2, atms.body()!!.data.items.size)
        val atmMachine : AtmMachine = atms.body()!!.data.items[0]
        Assert.assertEquals("atmid 5013", "5013", atmMachine.atmId)
        Assert.assertEquals("address desc", "address desc", atmMachine.addressDesc)
        Assert.assertEquals("bills", "20.00", atmMachine.bills)
        Assert.assertEquals("city", "NY", atmMachine.city)
        Assert.assertEquals("currency", "USD", atmMachine.currency)
        Assert.assertEquals("atm desc", "atm desc", atmMachine.desc)
        Assert.assertEquals("detail", "address detail", atmMachine.detail)
        Assert.assertEquals("fees", null,atmMachine.fees)
        Assert.assertEquals("latitude ", "30.00",atmMachine.latitude)
        Assert.assertEquals("longitude ", "-30.00",atmMachine.longitude)
        Assert.assertEquals("max", "500.00", atmMachine.max)
        Assert.assertEquals("min", "20.00", atmMachine.min)
        Assert.assertEquals("redemption", 0, atmMachine.redemption)
        Assert.assertEquals("zip", "123",atmMachine.zip)
    }
}
