package com.akexorcist.ruammij.data

import android.graphics.drawable.Drawable
import com.akexorcist.ruammij.common.Installer

data class InstalledApp(
    val name: String,
    val packageName: String,
    val appVersion: String,
    val icon: Drawable?,
    val systemApp: Boolean,
    val installedAt: Long?,
    val installer: Installer,
    val sha256: String,
    val permissions: List<PermissionInfo> = listOf(),
)
