package cash.just.sdk

import android.util.Log
import cash.just.sdk.Cash.BtcNetwork
import cash.just.sdk.Cash.BtcNetwork.MAIN_NET
import cash.just.sdk.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CashImpl:Cash {
    private lateinit var sessionKey:String
    private lateinit var retrofit: WacAPI
    private lateinit var serverUrl:BtcNetwork

    override fun createGuestSession(network: BtcNetwork, listener: Cash.SessionCallback) {
        initIfNeeded(network)

        retrofit.guestLogin().enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                listener.onError(t.message)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    sessionKey = response.body()!!.data.sessionKey
                    listener.onSessionCreated(sessionKey)
                } else {
                    var message = response.code().toString()
                    if (response.code() == 502) {
                        message = "$message: Service Temporary Not Available"
                    }
                    listener.onError(message)
                }
            }
        })
    }

    private fun initRetrofit(network: BtcNetwork){
        val serverUrl = when(network) {
            MAIN_NET -> {
                "https://api-prd.just.cash/"
            }
            BtcNetwork.TEST_NET -> {
                "https://secure.just.cash/"
            }
            BtcNetwork.TEST_LOCAL-> {
                "http://127.0.0.1:8080/"
            }
        }

        retrofit = Retrofit.Builder().baseUrl(serverUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(WacAPI::class.java)
    }

    private fun initIfNeeded(network: BtcNetwork) {
        if (!::retrofit.isInitialized) {
            initRetrofit(network)
        } else if(!::serverUrl.isInitialized || serverUrl != network) {
            initRetrofit(network)
        }
    }

    override fun isSessionCreated() : Boolean {
        return this::sessionKey.isInitialized
    }

    override fun login(network: BtcNetwork, phoneNumber: String, listener: Cash.SessionCallback) {
        initIfNeeded(network)

        retrofit.login(phoneNumber).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                   Log.d("cash", response.body()?.toString())
                } else {
                    Log.d("cash", response.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                listener.onError(t.message)
            }
        })
    }

    override fun register(network: BtcNetwork, phoneNumber: String, firstName: String, lastName: String, listener: Cash.RegistrationCallback) {
        initIfNeeded(network)

        retrofit.register(phoneNumber, firstName, lastName).enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Log.d("cash", response.body()?.toString())
                } else {
                    Log.d("cash", response.errorBody()?.toString())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                listener.onError(t.message)
            }
        })
    }

    override fun getAtmList(): Call<AtmListResponse> {
        return retrofit.getAtmList(sessionKey)
    }

    override fun getAtmListByLocation(latitude: String, longitude: String): Call<AtmListResponse> {
        return retrofit.getAtmListByLocation(sessionKey, latitude, longitude)
    }

    override fun checkCashCodeStatus(code: String): Call<CashCodeStatusResponse> {
        return retrofit.checkCodeStatus(code, sessionKey)
    }

    override fun createCashCode(atmId: String, amount: String, verificationCode: String): Call<CashCodeResponse> {
        return retrofit.createCode(sessionKey, atmId, amount, verificationCode)
    }

    override fun sendVerificationCode(
        firstName: String,
        lastName: String,
        phoneNumber: String?,
        email: String?
    ): Call<SendVerificationCodeResponse> {
        return retrofit.sendVerificationCode(sessionKey, firstName, lastName, phoneNumber, email)
    }
}