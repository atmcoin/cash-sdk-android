package cash.just.sdk.app

import android.app.Application
import cash.just.sdk.Cash
import timber.log.Timber

class App: Application() {
    var server: Cash.BtcNetwork = Cash.BtcNetwork.TEST_NET
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}