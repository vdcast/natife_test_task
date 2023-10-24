package com.example.natifetesttask.data.local.prefs

import android.content.Context
import javax.inject.Inject

class SharedPrefs @Inject constructor(context: Context) {
    private val FIRST_OPEN = "first_open"

    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    fun setFirstOpen(firstOpen: Boolean) { prefs.edit().putBoolean(FIRST_OPEN, firstOpen).apply() }
    fun getFirstOpen(): Boolean = prefs.getBoolean(FIRST_OPEN, false)
}