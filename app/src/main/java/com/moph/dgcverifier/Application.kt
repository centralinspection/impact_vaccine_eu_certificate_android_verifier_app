package com.owner.dgcverifier

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import java.lang.ref.WeakReference

class Application: Application() {
    companion object {
        lateinit var SHARED_PREFERENCES: SharedPreferences
        lateinit var CONTEXT: WeakReference<Context>
    }

    override fun onCreate() {
        super.onCreate()
        SHARED_PREFERENCES = PreferenceManager.getDefaultSharedPreferences(this)
        CONTEXT = WeakReference(applicationContext)
        DGCCertificateManager
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}