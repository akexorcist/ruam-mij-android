package com.akexorcist.ruammij.base.utility

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Long?.toReadableDatetime(): String {
    this ?: return stringResource(R.string.app_installed_at_unknown)
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        .withLocale(Locale.getDefault())
    return dateTime.format(formatter)
}
