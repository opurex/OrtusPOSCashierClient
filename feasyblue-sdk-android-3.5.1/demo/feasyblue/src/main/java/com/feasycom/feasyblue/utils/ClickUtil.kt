package com.feasycom.feasyblue.utils

object ClickUtil {
    private var lastClickTime: Long = 0

    @Synchronized
    fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 1000){
            return true
        }
        lastClickTime = time
        return false
    }
}