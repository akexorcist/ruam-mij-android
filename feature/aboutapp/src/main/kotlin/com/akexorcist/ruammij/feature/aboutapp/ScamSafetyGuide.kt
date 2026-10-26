@file:OptIn(ExperimentalMaterial3Api::class)

package com.akexorcist.ruammij.feature.aboutapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

private data class SafetyTopic(val id: String, val title: Int, val body: Int)

private val safetyTopics = listOf(
    SafetyTopic("impersonation", R.string.safety_impersonation_title, R.string.safety_impersonation_body),
    SafetyTopic("links", R.string.safety_links_title, R.string.safety_links_body),
    SafetyTopic("device", R.string.safety_device_title, R.string.safety_device_body),
    SafetyTopic("money", R.string.safety_money_title, R.string.safety_money_body),
    SafetyTopic("accounts", R.string.safety_accounts_title, R.string.safety_accounts_body),
    SafetyTopic("android", R.string.safety_android_title, R.string.safety_android_body),
    SafetyTopic("limits", R.string.safety_limits_title, R.string.safety_limits_body),
)

private val safetySources = listOf(
    R.string.safety_source_aoc to "https://mdes.go.th/mission/153/",
    R.string.safety_source_qr to "https://consumer.ftc.gov/consumer-alerts/2026/09/see-qr-code-parked-somewhere-dont-scan-ityet",
    R.string.safety_source_tasks to "https://consumer.ftc.gov/consumer-alerts/2025/08/how-spot-avoid-task-scams",
    R.string.safety_source_recovery to "https://consumer.ftc.gov/consumer-alerts/2026/08/have-you-lost-money-scam-watch-scammers-who-say-they-can-help",
    R.string.safety_source_play to "https://support.google.com/android/answer/2812853?hl=en",
    R.string.safety_source_calls to "https://blog.google/security/android-fake-call-detection/",
    R.string.safety_source_accounts to "https://consumer.ftc.gov/articles/what-do-if-you-were-scammed",
)

@Composable
fun ScamSafetyGuideBottomSheet(
    onDismissRequest: () -> Unit,
    onOpenSource: (String) -> Unit,
    onDialHelp: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.95f).navigationBarsPadding()) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(
                    text = stringResource(R.string.safety_guide_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(R.string.installed_app_display_option_close))
                }
            }
            Text(
                text = stringResource(R.string.safety_guide_updated),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            LazyColumn(Modifier.weight(1f).padding(horizontal = 16.dp)) {
                item(key = "urgent") {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(stringResource(R.string.safety_urgent_title), style = MaterialTheme.typography.titleMedium)
                            OutlinedButton(onClick = onDialHelp) { Text(stringResource(R.string.safety_dial_aoc)) }
                            Text(stringResource(R.string.safety_urgent_body), modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                    Text(
                        stringResource(R.string.safety_guide_offline),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
                items(safetyTopics, key = { it.id }) { topic ->
                    var expanded by rememberSaveable(topic.id) { mutableStateOf(false) }
                    val state = stringResource(if (expanded) R.string.safety_expanded else R.string.safety_collapsed)
                    Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        TextButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth().semantics { stateDescription = state },
                        ) {
                            Text(stringResource(topic.title), modifier = Modifier.weight(1f))
                            Text(if (expanded) "−" else "+")
                        }
                        if (expanded) {
                            Text(stringResource(topic.body), modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
                        }
                    }
                }
                item(key = "sources") {
                    Text(stringResource(R.string.safety_sources_title), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.safety_sources_description), style = MaterialTheme.typography.bodySmall)
                    safetySources.forEach { (label, url) ->
                        TextButton(onClick = { onOpenSource(url) }, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(label), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
