package cash.just.sdk.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.IllegalArgumentException

@JsonClass(generateAdapter = true)
data class KycStatusResponse(
    @field:Json(name = "result") val result: String,
    @field:Json(name = "error") val error: WacError?,
    @field:Json(name = "data") val data: KycData)

@JsonClass(generateAdapter = true)
data class KycData(@field:Json(name = "items") val items: List<KycItem>)

@JsonClass(generateAdapter = true)
data class KycItem(
    @field:Json(name = "status") val status: KycStatus,
    @field:Json(name = "pur_daily_count_limit") val pur_daily_count_limit: String,
    @field:Json(name = "pur_daily_amount_limit") val purchaseDailyAmountLimit: String,
    @field:Json(name = "pur_tx_amount_limit") val purchaseTxAmountLimit: String,
    @field:Json(name = "pur_remain_count") val purchaserRemainCount: String,
    @field:Json(name = "pur_remain_amount") val purchaseRemainAmount: String,
    @field:Json(name = "red_daily_count_limit") val redemptionDailyCountLimit: String,
    @field:Json(name = "red_daily_amount_limit") val redemptionDailyAmountLimit: String,
    @field:Json(name = "red_tx_amount_limit") val redemptionTxAmountLimit: String,
    @field:Json(name = "red_remain_count") val redemptionRemainCount: String,
    @field:Json(name = "red_remain_amount") val redemptionRemainAmount: String,
    @field:Json(name= "has_pi") val hasPi: String,
    @field:Json(name= "docs_pending") val docsPending: String,
    @field:Json(name= "docs_rejected") val docsRejected: String,
    @field:Json(name= "docs_accepted") val docsAccepted: String,
    @field:Json(name= "docs_required") val docsRequired: String,
    @field:Json(name= "first_name") val firstName: String,
    @field:Json(name= "last_name") val lastName: String)

enum class KycStatus (val statusCode: String) {
    NEW("NEW"),
    DOCS_VERIFIED("DOCS_VERIFIED"),
    REJECTED("REJECTED");

    companion object {
        fun resolve(status:String) : KycStatus {
            values().find { it.statusCode == status }?.let {
                return it
            }?:run {
                throw IllegalArgumentException("not valid code status {$status}")
            }
        }
    }
}