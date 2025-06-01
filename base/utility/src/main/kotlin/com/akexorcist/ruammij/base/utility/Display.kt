package com.akexorcist.ruammij.base.utility

import android.annotation.SuppressLint
import android.view.Display

@SuppressLint("PrivateApi")
fun Display.getOwnerPackageName(): String? {
    return try {
        val declaredMethod = this.javaClass.getDeclaredMethod("getOwnerPackageName")
        declaredMethod.invoke(this) as? String
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
