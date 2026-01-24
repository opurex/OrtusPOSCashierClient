package com.feasycom.feasyblue.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.swallowsonny.convertextlibrary.hex2ByteArray
import com.swallowsonny.convertextlibrary.toHexString
import java.util.*

private const val FILE_NAME = "feasycom"

private const val KEY_ORDERED_STRING_SET = "OrderedStringSet"

private fun Context.preference() =
    getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

fun Context.getBoolean(key: String, default: Boolean = false) =
    preference().getBoolean(key, default)

fun Context.getStr(key: String, default: String = "") =
    preference().getString(key, default) ?: default

fun Context.getInt(key: String, default: Int = 0) =
    preference().getInt(key, default)

fun Context.getLong(key: String, default: Long = 0L) =
    preference().getLong(key, default)

fun Context.getStrSet(key: String, data: HashSet<String>) =
    preference().getStringSet(key, data) as HashSet<String>

fun Context.putBoolean(key: String, value: Boolean) = preference().edit {
    putBoolean(key, value)
}

fun Context.putStr(key: String, value: String) = preference().edit {
    putString(key, value)
}

fun Context.putInt(key: String, value: Int) = preference().edit{
    putInt(key, value)
}

fun Context.putLong(key: String, value: Long) = preference().edit {
    putLong(key, value)
}

fun Context.putStrSet(key: String, value: HashSet<String>) = preference().edit {
    putStringSet(key, value)
}

fun Context.putByteArray(key: String, value: ByteArray) = preference().edit {
    putStr(key, value.toHexString(true))
}

fun saveOrderedStringSet(context: Context, orderedStringSet: LinkedHashSet<String>) {
    val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val gson = Gson()
    val json = gson.toJson(orderedStringSet)
    editor.putString(KEY_ORDERED_STRING_SET, json)
    editor.apply()
}

fun getOrderedStringSet(context: Context): LinkedHashSet<String> {
    val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    val gson = Gson()
    val json = prefs.getString(KEY_ORDERED_STRING_SET, null)
    return if (json != null) {
        val type = object : TypeToken<LinkedHashSet<String>>() {}.type
        gson.fromJson(json, type)
    } else {
        LinkedHashSet()
    }
}

fun Context.getByteArray(key: String, value: ByteArray = byteArrayOf()): ByteArray {
    val string = preference().getString(key, "")

    return if (string == ""){
        value
    }else {
        try {
            string!!.hex2ByteArray()
        }catch (e: Exception){
            value
        }
    }
}
