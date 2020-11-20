package cash.just.sdk.app

import android.app.Activity
import cash.just.sdk.Cash

fun Activity.getServer() : Cash.BtcNetwork {
    return (application as App).server
}