package cash.just.sdk.model

import retrofit2.Call
import retrofit2.http.*

interface WacAPI {
    @POST("/atm/wac/guest/login")
    fun login(): Call<LoginResponse>

    @GET("/atm/wac/atm/list")
    fun getAtmList(@Header("sessionKey") sessionKey: String): Call<AtmListResponse>

    @GET("/atm/wac/atm/near/latlon/{lat}/{lon}")
    fun getAtmListByLocation(
        @Header("sessionKey") sessionKey: String,
        @Path(value="lat", encoded=true) lat:String,
        @Path(value="lon", encoded=true) lon:String): Call<AtmListResponse>

    @GET("/atm/wac/pcode/{pcode}")
    fun checkCodeStatus(@Path(value="pcode", encoded=true) code:String, @Header("sessionKey") sessionKey: String): Call<CashCodeStatusResponse>

    @POST("/atm/wac/pcode")
    fun createCode(
        @Header("sessionKey") sessionKey: String,
        @Query(value="atm_id", encoded=true) atmId:String,
        @Query(value="amount", encoded=true) amount:String,
        @Query(value="verification_code", encoded=true) verificationCode:String): Call<CashCodeResponse>

    @POST("/atm/wac/pcode/verify")
    fun sendVerificationCode(
        @Header("sessionKey") sessionKey: String,
        @Query(value="first_name", encoded=true) firstName:String,
        @Query(value="last_name", encoded=true) lastName:String,
        @Query(value="phone_number", encoded=true) phoneNumber:String?,
        @Query(value="email", encoded=true) email:String?
    ): Call<SendVerificationCodeResponse>
}
