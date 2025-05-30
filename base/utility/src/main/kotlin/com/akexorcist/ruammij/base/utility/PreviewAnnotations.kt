package com.akexorcist.ruammij.base.utility

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    group = "Dark Light",
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL,
    showBackground = true,
    locale = "en",
)
@Preview(
    name = "Light",
    group = "Dark Light",
    uiMode = UI_MODE_NIGHT_NO or UI_MODE_TYPE_NORMAL,
    showBackground = true,
    locale = "th",
)
@Preview(
    name = "Dark",
    group = "Dark Light",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    locale = "en",
)
@Preview(
    name = "Dark",
    group = "Dark Light",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    locale = "th",
)
annotation class DarkLightPreviews
