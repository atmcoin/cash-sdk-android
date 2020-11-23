package cash.just.sdk

import cash.just.sdk.model.*
import retrofit2.Call

object CashSDK : Cash {
    private var cashImpl : Cash = CashImpl()

    override fun createGuestSession(network: Cash.BtcNetwork, listener: Cash.SessionCallback) {
        cashImpl.createGuestSession(network, listener)
    }

    override fun isSessionCreated(): Boolean {
       return cashImpl.isSessionCreated()
    }

    override fun login(network: Cash.BtcNetwork, phoneNumber: String, listener: Cash.WacCallback) {
        requireSession()
        return cashImpl.login(network, phoneNumber, listener)
    }

    override fun register(network: Cash.BtcNetwork, phoneNumber: String, firstName: String, lastName: String, listener: Cash.WacCallback) {
        requireSession()
        return cashImpl.register(network, phoneNumber, firstName, lastName, listener)
    }

    override fun getAtmList(): Call<AtmListResponse> {
        requireSession()
        return cashImpl.getAtmList()
    }

    override fun getAtmListByLocation(latitude:String, longitude:String): Call<AtmListResponse> {
        requireSession()
        return cashImpl.getAtmListByLocation(latitude, longitude)
    }

    override fun checkCashCodeStatus(code:String): Call<CashCodeStatusResponse> {
        requireSession()
        return cashImpl.checkCashCodeStatus(code)
    }

    override fun createCashCode(atmId:String, amount:String, verificationCode:String): Call<CashCodeResponse> {
        requireSession()
        return cashImpl.createCashCode(atmId, amount, verificationCode)
    }

    override fun sendVerificationCode(firstName:String, lastName:String, phoneNumber:String?, email:String?): Call<SendVerificationCodeResponse> {
        requireSession()
        return cashImpl.sendVerificationCode(firstName, lastName, phoneNumber, email)
    }

    override fun getKycStatus(): Call<KycStatusResponse> {
        requireSession()
        return cashImpl.getKycStatus()
    }

    override fun loginConfirm(confirmNumber: String, listener: Cash.WacCallback) {
        requireSession()
        return cashImpl.loginConfirm(confirmNumber, listener)
    }

    private fun requireSession() {
        if (!cashImpl.isSessionCreated())
            throw IllegalStateException("session was not created, did you call #createGuestSession()?")
    }
}