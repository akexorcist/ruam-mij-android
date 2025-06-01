package com.akexorcist.ruammij.feature.unverified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.base.ui.component.DescriptionText
import com.akexorcist.ruammij.base.ui.component.HeadlineText
import com.akexorcist.ruammij.base.ui.theme.Buttons
import com.akexorcist.ruammij.base.ui.theme.RuamMijTheme


@Composable
private fun UnverifiedInstallerContent(
    onDownloadButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
    ) {
        HeadlineText(text = stringResource(R.string.landing_tips_title))
        Spacer(modifier = Modifier.height(8.dp))
        DescriptionText(text = stringResource(R.string.landing_tips_description))
        Spacer(modifier = Modifier.height(16.dp))
        DownloadButton(onClick = onDownloadButtonClick)
    }
}

@Composable
private fun DownloadButton(onClick: () -> Unit) {
    OutlinedButton(
        contentPadding = Buttons.ContentPadding,
        onClick = onClick,
    ) {
        Text(text = stringResource(R.string.landing_tips_open_google_play))
    }
}

@Preview
@Composable
private fun UnverifiedInstallerContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            UnverifiedInstallerContent(
                onDownloadButtonClick = {},
            )
        }
    }
}
