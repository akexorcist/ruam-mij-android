package com.akexorcist.ruammij.feature.aboutapp

import androidx.appcompat.app.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.os.*
import com.akexorcist.ruammij.base.ui.component.BodyText
import com.akexorcist.ruammij.base.ui.component.BoldBodyText
import com.akexorcist.ruammij.base.ui.theme.RuamMijTheme
import java.util.Locale

@Composable
fun LanguageDropdown() {
    var expanded by remember { mutableStateOf(false) }
    val selectedLocale = getCurrentLocale()

    LanguageDropdownItem(
        expanded = expanded,
        selectedLocale = selectedLocale,
        onToggle = { expanded = it }
    )
}

@Composable
private fun LanguageDropdownItem(
    expanded: Boolean,
    selectedLocale: Locale,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .clip(shape = RoundedCornerShape(6.dp))
            .clickable { onToggle(true) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BodyText(text = stringResource(R.string.about_app_menu_language))

        Spacer(modifier = Modifier.Companion.weight(1f))

        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoldBodyText(
                    text = when (selectedLocale.isThai()) {
                        true -> stringResource(R.string.about_app_language_thai)
                        false -> stringResource(R.string.about_app_language_english)
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(R.string.about_app_menu_language),
                    tint = MaterialTheme.colorScheme.onSurface,
                    imageVector = Icons.Rounded.ArrowDropDown,
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onToggle(false) },
            ) {
                DropdownMenuItem(
                    onClick = {
                        onToggle(false)
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                    },
                    text = { BoldBodyText(text = stringResource(R.string.about_app_language_english)) },
                )
                DropdownMenuItem(
                    onClick = {
                        onToggle(false)
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("th"))
                    },
                    text = { BoldBodyText(text = stringResource(R.string.about_app_language_thai)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun LanguageDropdownButtonPreview() {
    RuamMijTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            LanguageDropdownItem(
                expanded = false,
                selectedLocale = Locale.US,
                onToggle = { }
            )
        }
    }
}

private fun Locale.isThai(): Boolean {
    return this.toLanguageTag().contains("th")
}

private fun getCurrentLocale(): Locale {
    val locales = AppCompatDelegate.getApplicationLocales().takeIf { !it.isEmpty }
        ?: LocaleListCompat.getDefault()
    return locales.takeIf { !it.isEmpty }
        ?.get(0)
        ?: Locale.US
}
