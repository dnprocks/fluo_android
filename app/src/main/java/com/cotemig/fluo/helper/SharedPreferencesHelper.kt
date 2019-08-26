package com.cotemig.fluo.helper

import android.content.Context

object SharedPreferencesHelper {

    fun saveString(context: Context, filename: String, key: String, value: String) {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readString(context: Context, filename: String, key: String): String? {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return preferences.getString(key, null)
    }

    fun saveBoolean(context: Context, filename: String, key: String, value: Boolean?) {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun readBoolean(context: Context, filename: String, key: String, defaultValue: Boolean): Boolean {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return preferences.getBoolean(key, defaultValue)
    }

    fun saveInt(context: Context, filename: String, key: String, value: Int) {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun readInt(context: Context, filename: String, key: String, defaultValue: Int): Int {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return preferences.getInt(key, defaultValue)
    }

    fun delete(context: Context, filename: String, key: String) {
        val preferences = context.applicationContext.getSharedPreferences(filename, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(key)
        editor.apply()
    }
}