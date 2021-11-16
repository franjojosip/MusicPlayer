package hr.fjjukic.template.app.base.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import hr.fjjukic.template.app.BuildConfig
import hr.fjjukic.template.app_common.shared_pref.AppSharedPreference

class AppSharedPreferenceImpl(applicationContext: Context, private val gson: Gson) :
    AppSharedPreference {
    private val defaultSharedPref: SharedPreferences

    init {
        val packageName = applicationContext.packageName
        defaultSharedPref = applicationContext.getSharedPreferences(
            "$packageName$DEFAULT_SHARED_PREF",
            Context.MODE_PRIVATE
        )
        editor { putString(KEY_API_URL, BuildConfig.API_URL) }
    }

    private fun editor(editor: SharedPreferences.Editor.() -> Unit) {
        defaultSharedPref.edit().also(editor).apply()
    }
}
