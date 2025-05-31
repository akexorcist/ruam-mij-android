package com.akexorcist.ruammij.base.data

import android.graphics.drawable.Drawable

data class PermissionInfo(
    val name: String,
    val label: String,
    val description: String,
    val icon: Drawable? = null,
)