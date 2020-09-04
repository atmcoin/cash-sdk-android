package cash.just.sdk

import cash.just.sdk.model.AtmListResponse
import cash.just.sdk.model.CashCodeResponse
import cash.just.sdk.model.CashCodeStatusResponse
import cash.just.sdk.model.SendVerificationCodeResponse
import retrofit2.Call

interface Cash {

    enum class BtcNetwork {
        MAIN_NET,
        TEST_NET,
        TEST_LOCAL
    }

    interface SessionCallback {
        fun onSessionCreated(sessionKey:String)
        fun onError(errorMessage:String?)
    }

    interface RegistrationCallback {
        fun onRegistered()
        fun onError(errorMessage:String?)
    }

    fun createGuestSession(network:BtcNetwork, listener:SessionCallback)
    fun isSessionCreated(): Boolean
    fun login(network: BtcNetwork, phoneNumber: String, listener:SessionCallback)
    fun register(network: BtcNetwork, phoneNumber: String, firstName:String, lastName:String, listener:RegistrationCallback)
    fun getAtmList(): Call<AtmListResponse>
    fun getAtmListByLocation(latitude:String, longitude:String): Call<AtmListResponse>
    fun checkCashCodeStatus(code:String): Call<CashCodeStatusResponse>
    fun createCashCode(atmId:String, amount:String, verificationCode:String): Call<CashCodeResponse>
    fun sendVerificationCode(firstName:String, lastName:String, phoneNumber:String?, email:String?): Call<SendVerificationCodeResponse>
}