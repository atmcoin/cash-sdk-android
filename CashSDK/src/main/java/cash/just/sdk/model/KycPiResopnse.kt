package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KycPiResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: WacError?,
    @field:Json(name = "data") val data: KycData)

@JsonClass(generateAdapter = true)
data class KycPiData(@field:Json(name = "items") val items: List<KycPiItem>)

@JsonClass(generateAdapter = true)
data class KycPiItem(
    @field:Json(name = "ssn") val ssn: String,
    )