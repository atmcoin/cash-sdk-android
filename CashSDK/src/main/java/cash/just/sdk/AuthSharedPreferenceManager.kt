package cash.just.sdk

import android.content.Context
import android.content.SharedPreferences

object AuthSharedPreferenceManager {
        private const val APP_SETTINGS = "APP_AUTH_PREFERENCES"
        private const val PHONE = "PHONE"
        private const val SESSION_KEY = "SESSION_KEY"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        }

        fun getPhone(context: Context): String? {
            return getSharedPreferences(context).getString(PHONE, null)
        }

        fun setPhone(context: Context, phone: String) {
            getSharedPreferences(context).edit().putString(PHONE, phone).apply()
        }

        fun getSession(context: Context): String? {
            return getSharedPreferences(context).getString(SESSION_KEY, null)
        }

        fun setSession(context: Context, phone: String) {
            getSharedPreferences(context).edit().putString(SESSION_KEY, phone).apply()
        }
}