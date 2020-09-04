package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GuestResponse(@field:Json(name = "result") val result: String,
     @field:Json(name = "error") val error: String?,
     @field:Json(name = "data") val data: GuestData
)

@JsonClass(generateAdapter = true)
data class GuestData(@field:Json(name = "sessionKey") val sessionKey: String)