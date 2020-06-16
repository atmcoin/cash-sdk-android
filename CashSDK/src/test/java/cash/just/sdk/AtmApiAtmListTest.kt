package cash.just.sdk

import android.widget.Toast
import cash.just.sdk.model.AtmListResponse
import com.github.tomakehurst.wiremock.client.WireMock
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import retrofit2.Call
import retrofit2.Response
import com.github.tomakehurst.wiremock.client.WireMock.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AtmApiAtmListTest() : AbstractAtmApiTest() {


    var RESPONSE_LIST_ATMS =
        "{ \"result\": \"ok\", \"error\": null, \"data\": { \"items\": [ { \"atm_id\": \"5013\", \"address_desc\": \"4219 MAST CT, Land O Lakes\", \"address_street\": \"Mast Court\", \"address_detail\": null, \"address_city\": \"Land O Lakes\", \"address_state\": \"FL\", \"address_zip\": \"34639\", \"loc_lon\": \"-82.4507400\", \"loc_lat\": \"28.2179330\", \"atm_desc\": \"4219 MAST CT\", \"atm_min\": \"20.00\", \"atm_max\": \"500.00\", \"atm_bills\": \"20.00\", \"atm_currency\": \"USD\", \"atm_red\": \"0\" }, { \"atm_id\": \"5014\", \"address_desc\": \"4601 BASSWOOD ST, Land O Lakes\", \"address_street\": \"Grand Oaks Boulevard\", \"address_detail\": null, \"address_city\": \"Land O Lakes\", \"address_state\": \"FL\", \"address_zip\": \"34639\", \"loc_lon\": \"-82.3846080\", \"loc_lat\": \"28.2228480\", \"atm_desc\": \"4601 BASSWOOD ST\", \"atm_min\": \"20.00\", \"atm_max\": \"400.00\", \"atm_bills\": \"20.00\", \"atm_currency\": \"USD\", \"atm_red\": \"1\" } ] } }"

    @Test
    fun RestAtmListSuccess() {
        System.out.println("Start : RestAtmListSuccess" )
        InitSession()

        stubFor(
            get(urlEqualTo("/atm/wac/atm/list"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = "";
        lateinit  var atms: Response<AtmListResponse>
        CashSDK.getAtmList().enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                atms = response;

            }
        })

        Thread.sleep(500)

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", atms);
        Assert.assertTrue("isSuccessful" + error, atms.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));

        Assert.assertEquals("List item size 2", 2,atms.body()!!.data.items.size);
        Assert.assertEquals("atmid 5013", "5013",atms.body()!!.data.items.get(0).atmId);
    }

    @Test
    fun RestAtmListError() {
        System.out.println("Start : RestAtmListError" )
        InitSession()

        stubFor(
            get(urlEqualTo("/atm/wac/atm/list"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(502)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = "";
        lateinit  var atms: Response<AtmListResponse>
        CashSDK.getAtmList().enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                atms = response;

            }
        })

        Thread.sleep(500)

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", atms);
        Assert.assertFalse("isSuccessful" + error, atms.isSuccessful);

    }


    //@Test
    fun RestAtmListNearSuccess() {
        System.out.println("Start : RestAtmListNearSuccess" )
        InitSession()

        stubFor(
            get(urlEqualTo("/atm/wac/atm/near/latlon/1/2"))
                .withHeader("sessionKey",equalTo(SESSION_KEY))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(RESPONSE_LIST_ATMS)
                )
        )

        var error: String? = "";
        lateinit  var atms: Response<AtmListResponse>
        CashSDK.getAtmListByLocation("2","1").enqueue(object : retrofit2.Callback<AtmListResponse> {
            override fun onFailure(call: Call<AtmListResponse>, t: Throwable) {
                error = t.message
            }

            override fun onResponse(call: Call<AtmListResponse>, response: Response<AtmListResponse>) {
                atms = response;

            }
        })

        Thread.sleep(500)

        System.out.println("Error : " + error)
        Assert.assertNotNull("Session  created ", atms);
        Assert.assertTrue("isSuccessful" + error, atms.isSuccessful);
        Assert.assertTrue("Error  Empty " + error, StringUtils.isBlank(error));

        Assert.assertEquals("List item size 2", 2,atms.body()!!.data.items.size);
        Assert.assertEquals("atmid 5013", "5013",atms.body()!!.data.items.get(0).atmId);
    }
}
