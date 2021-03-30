package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.IllegalArgumentException

@JsonClass(generateAdapter = true)
data class KycDocTypeResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: WacError?,
    @field:Json(name = "data") val data: KycData)

@JsonClass(generateAdapter = true)
data class KycDocTypeData(@field:Json(name = "items") val items: List<KycDocTypeItem>)

@JsonClass(generateAdapter = true)
data class KycDocTypeItem(
    @field:Json(name = "code") val status: KycCode,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "instructions") val instructions: String)

enum class KycCode(private val statusCode:String){
    ID_BACK("ID_BACK"),
    ID_FRONT("ID_FRONT"),
    SELFIE("SELFIE");

    companion object {
        fun resolve(status:String) : KycCode {
            values().find { it.statusCode == status }?.let {
                return it
            }?:run {
                throw IllegalArgumentException("not valid code status {$status}")
            }
        }
    }
}