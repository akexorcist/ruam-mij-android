@file:OptIn(ExperimentalMaterial3Api::class)

package com.akexorcist.ruammij.ui.aboutapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.BuildConfig
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Contributors
import com.akexorcist.ruammij.ui.component.BodyText
import com.akexorcist.ruammij.ui.component.BoldBodyText
import com.akexorcist.ruammij.ui.component.DescriptionText
import com.akexorcist.ruammij.ui.component.HeadlineText
import com.akexorcist.ruammij.ui.component.LanguageDropdownButton
import com.akexorcist.ruammij.ui.theme.Buttons
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun AboutAppRoute() {
    val activity = LocalContext.current as Activity
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    var showContributor by remember { mutableStateOf(false) }

    when {
        showPrivacyPolicy -> PrivacyPolicyBottomSheet(
            onDismissRequest = { showPrivacyPolicy = false }
        )

        showContributor -> ContributorBottomSheet(
            onDismissRequest = { showContributor = false }
        )
    }

    AboutAppScreen(
        onFaqClick = {},
        onPrivacyPolicyClick = {
            showPrivacyPolicy = true
        },
        onContributorClick = {
            showContributor = true
        },
        onOpenSourceLicenseClick = {
            activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(
                activity.getString(R.string.about_app_menu_open_source_licenses)
            )
        },
        onSourceCodeClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/akexorcist/ruam-mij-android"))
            activity.startActivity(intent)
        },
    )
}

@Composable
private fun AboutAppScreen(
    onFaqClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onOpenSourceLicenseClick: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onContributorClick: () -> Unit,
) {
    val appVersion by remember { mutableStateOf("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Header()

        Spacer(modifier = Modifier.height(16.dp))

        LanguageDropdownButton()

        Spacer(modifier = Modifier.height(16.dp))

//        MenuItem(
//            label = stringResource(R.string.about_app_menu_frequently_asked_questions),
//            icon = painterResource(R.drawable.ic_about_app_faq),
//            onClick = onFaqClick,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            label = stringResource(R.string.about_app_menu_privacy_policy),
            icon = painterResource(R.drawable.ic_about_app_privacy_policy),
            onClick = onPrivacyPolicyClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            label = stringResource(R.string.about_app_menu_contributor),
            icon = painterResource(R.drawable.ic_about_app_contributor),
            onClick = onContributorClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            label = stringResource(R.string.about_app_menu_open_source_licenses),
            icon = painterResource(R.drawable.ic_about_app_source_code),
            onClick = onOpenSourceLicenseClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            label = stringResource(R.string.about_app_menu_source_code),
            icon = painterResource(R.drawable.ic_about_app_source_code),
            onClick = onSourceCodeClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DescriptionText(
            text = stringResource(R.string.about_app_version, appVersion),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))
        HeadlineText(
            text = stringResource(R.string.app_name),
        )
        Spacer(modifier = Modifier.height(2.dp))
        DescriptionText(
            text = stringResource(R.string.app_description),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun MenuItem(
    label: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .clip(shape = RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            BodyText(text = label)
        }
        Icon(
            modifier = Modifier.size(24.dp),
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = stringResource(R.string.description_see_more, label),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ContributorBottomSheet(
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = onDismissRequest,
    ) {
        ContributorContent(
            onCloseClick = onDismissRequest,
        )
    }
}

@Composable
private fun ContributorContent(
    onCloseClick: () -> Unit,
) {
    val contributors by remember { mutableStateOf(Contributors.contributors) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            HeadlineText(text = stringResource(R.string.about_app_menu_contributor))
            Spacer(modifier = Modifier.height(16.dp))
            contributors.forEach { contributor ->
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    BodyText(text = "• $contributor")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                BodyText(text = stringResource(R.string.about_app_menu_contributor_more))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outline)
                .height(1.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                contentPadding = Buttons.ContentPadding,
                onClick = onCloseClick,
            ) {
                BoldBodyText(text = stringResource(R.string.installed_app_display_option_close))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PrivacyPolicyBottomSheet(
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = onDismissRequest,
    ) {
        PrivacyPolicyContent(
            onCloseClick = onDismissRequest,
        )
    }
}

@Composable
private fun PrivacyPolicyContent(
    onCloseClick: () -> Unit,
) {
    val contents: List<PageContent.Item> = listOf(
        PageContent.Item.Content(stringResource(R.string.privacy_policy_introduction)),
        PageContent.Item.Spacing,
        PageContent.Item.Section(stringResource(R.string.privacy_policy_1_title)),
        PageContent.Item.Content(stringResource(R.string.privacy_policy_1_detail)),
        PageContent.Item.Bullet(stringResource(R.string.privacy_policy_1_item_1)),
        PageContent.Item.Bullet(stringResource(R.string.privacy_policy_1_item_2)),
        PageContent.Item.Spacing,
        PageContent.Item.Section(stringResource(R.string.privacy_policy_2_title)),
        PageContent.Item.Content(stringResource(R.string.privacy_policy_2_detail)),
        PageContent.Item.Spacing,
        PageContent.Item.Section(stringResource(R.string.privacy_policy_3_title)),
        PageContent.Item.Content(stringResource(R.string.privacy_policy_3_detail)),
        PageContent.Item.Spacing,
        PageContent.Item.Spacing,
        PageContent.Item.Spacing,
        PageContent.Item.Content(
            "${stringResource(R.string.privacy_policy_app_name)} (${stringResource(R.string.privacy_policy_year)})"
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            HeadlineText(text = stringResource(R.string.about_app_menu_privacy_policy))
            Spacer(modifier = Modifier.height(16.dp))
            contents.forEach { content ->
                when (content) {
                    is PageContent.Item.Section -> {
                        BoldBodyText(text = content.text)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    is PageContent.Item.Content -> {
                        BodyText(text = content.text)
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    is PageContent.Item.Bullet -> {
                        Row {
                            Spacer(modifier = Modifier.width(8.dp))
                            BodyText(text = "•")
                            Spacer(modifier = Modifier.width(8.dp))
                            BodyText(text = content.text)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    is PageContent.Item.Spacing -> {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outline)
                .height(1.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                contentPadding = Buttons.ContentPadding,
                onClick = onCloseClick,
            ) {
                BoldBodyText(text = stringResource(R.string.installed_app_display_option_close))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class PageContent(
    val items: List<Item>
) {
    sealed class Item {
        data class Section(val text: String) : Item()
        data class Content(val text: String) : Item()
        data class Bullet(val text: String) : Item()
        data object Spacing : Item()
    }
}

@Preview
@Composable
private fun AboutAppScreenPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            AboutAppScreen(
                onFaqClick = {},
                onPrivacyPolicyClick = {},
                onSourceCodeClick = {},
                onOpenSourceLicenseClick = {},
                onContributorClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ContributorContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            ContributorContent(
                onCloseClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PrivacyPolicyContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            PrivacyPolicyContent(
                onCloseClick = {},
            )
        }
    }
}
