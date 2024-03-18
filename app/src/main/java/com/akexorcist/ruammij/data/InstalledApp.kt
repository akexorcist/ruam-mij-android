package com.akexorcist.ruammij.data

import android.graphics.drawable.Drawable

data class InstalledApp(
    val name: String,
    val packageName: String,
    val appVersion: String,
    val icon: Drawable?,
    val systemApp: Boolean,
    val installedAt: Long?,
    val installer: String?,
)
