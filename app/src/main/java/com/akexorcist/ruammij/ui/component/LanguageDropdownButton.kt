package com.akexorcist.ruammij.ui.component

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
import androidx.compose.ui.unit.*
import androidx.core.os.*

const val ENGLISH_TEXT = "English"
const val THAI_TEXT = "ไทย"

@Composable
fun LanguageDropdownButton() {
    var expanded by remember { mutableStateOf(false) }
    val selectedLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()

    Row(modifier = Modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(6.dp),
        )
        .clip(shape = RoundedCornerShape(6.dp))
        .clickable { expanded = true }
        .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically) {


        BodyText(text = stringResource(id = com.akexorcist.ruammij.R.string.language))

        Spacer(modifier = Modifier.weight(1f))

        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoldBodyText(text = if (selectedLanguage.contains("th")) THAI_TEXT else ENGLISH_TEXT)


                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.size(24.dp),
                    contentDescription = "language",
                    tint = MaterialTheme.colorScheme.onSurface,
                    imageVector = Icons.Rounded.ArrowDropDown,
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                }, text = { BoldBodyText(ENGLISH_TEXT) })
                DropdownMenuItem(onClick = {
                    expanded = false
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("th"))
                }, text = { BoldBodyText(THAI_TEXT) })
            }
        }
    }


}