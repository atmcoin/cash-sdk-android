package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WacBaseResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: String?)