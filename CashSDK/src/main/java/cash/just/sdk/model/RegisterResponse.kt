package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: String?,
    @field:Json(name = "data") val data: RegisterData
)

@JsonClass(generateAdapter = true)
data class RegisterData(@field:Json(name = "result") val result: String)