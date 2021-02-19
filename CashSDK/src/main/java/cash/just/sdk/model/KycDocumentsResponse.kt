package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.IllegalArgumentException

@JsonClass(generateAdapter = true)
data class KycDocumentResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: WacError?,
    @field:Json(name = "data") val data: KycData)

@JsonClass(generateAdapter = true)
data class KycDocumentData(@field:Json(name = "items") val items: List<String>)