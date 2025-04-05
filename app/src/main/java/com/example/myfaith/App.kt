package com.example.myfaith

import android.app.Application
import android.content.Context
import com.example.myfaith.utils.LocaleHelper

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        val lang = base?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            ?.getString("lang", "kk") ?: "kk"
        val context = base?.let { LocaleHelper.setLocale(it, lang) }
        super.attachBaseContext(context)
    }
}
