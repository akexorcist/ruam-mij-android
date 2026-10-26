package com.akexorcist.ruammij.base.data

import com.akexorcist.ruammij.base.data.database.SafeApp

fun InstalledApp.withUserTrust(decisions: List<SafeApp>): InstalledApp = copy(
    isTrustedByUser = sha256.isNotBlank() && decisions.any {
        it.packageName == packageName && it.signingSha256 == sha256
    },
)
