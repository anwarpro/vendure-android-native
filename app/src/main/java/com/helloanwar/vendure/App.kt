package com.helloanwar.vendure

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        var sharedPref: SharedPreferences? = null
    }
}