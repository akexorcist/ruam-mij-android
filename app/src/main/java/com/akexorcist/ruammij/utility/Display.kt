package com.akexorcist.ruammij.utility

import android.annotation.SuppressLint
import android.view.Display

@SuppressLint("DiscouragedPrivateApi")
fun Display.getOwnerPackageName(): String? {
    return try {
        val declaredMethod = this.javaClass.getDeclaredMethod("getOwnerPackageName")
        declaredMethod.invoke(this) as? String
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
