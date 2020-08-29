package cash.just.sdk.model

import retrofit2.Call
import retrofit2.http.*

interface WacAPI {
    @POST("/wac/wac/guest/login")
    fun guestLogin(): Call<LoginResponse>

    @POST("/wac/wac/login")
    fun login(@Query(value="phone_number", encoded=true) phoneNumber:String): Call<LoginResponse>

    @POST("/wac/wac/login")
    fun register(@Query(value="phone_number", encoded=true) phoneNumber:String,
                 @Query(value="first_name", encoded=true) firstName:String,
                 @Query(value="last_name", encoded=true) lastName:String
    ): Call<LoginResponse>

    @GET("/wac/wac/atm/list")
    fun getAtmList(@Header("sessionKey") sessionKey: String): Call<AtmListResponse>

    @GET("/wac/wac/atm/near/latlon/{lat}/{lon}")
    fun getAtmListByLocation(
        @Header("sessionKey") sessionKey: String,
        @Path(value="lat", encoded=true) lat:String,
        @Path(value="lon", encoded=true) lon:String): Call<AtmListResponse>

    @GET("/wac/pcode/{pcode}")
    fun checkCodeStatus(@Path(value="pcode", encoded=true) code:String, @Header("sessionKey") sessionKey: String): Call<CashCodeStatusResponse>

    @POST("/wac/pcode")
    fun createCode(
        @Header("sessionKey") sessionKey: String,
        @Query(value="atm_id", encoded=true) atmId:String,
        @Query(value="amount", encoded=true) amount:String,
        @Query(value="verification_code", encoded=true) verificationCode:String): Call<CashCodeResponse>

    @POST("/wac/wac/pcode/verify")
    fun sendVerificationCode(
        @Header("sessionKey") sessionKey: String,
        @Query(value="first_name", encoded=true) firstName:String,
        @Query(value="last_name", encoded=true) lastName:String,
        @Query(value="phone_number", encoded=true) phoneNumber:String?,
        @Query(value="email", encoded=true) email:String?
    ): Call<SendVerificationCodeResponse>
}
