# Development and validation

Use JDK 21 and the Android SDK platform requested by `gradle/libs.versions.toml` (currently API 37). Set `JAVA_HOME` and `ANDROID_HOME` in the shell, or set the SDK path in the ignored `local.properties` file. Use the committed Gradle wrapper.

On Windows:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

Focused trust regression checks:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests '*UserTrustTest' --tests '*DeviceRepositoryTrustTest' --tests '*TrustMigrationTest'
```

Unit test reports are under `app/build/reports/tests/testDebugUnitTest/`. The debug APK is under `app/build/outputs/apk/debug/`. Debug builds use the separate application ID `com.akexorcist.ruammij.debug`, version suffix `-test`, and launcher label `Ruam Mij (Test)` / `รวมมิจ (ทดสอบ)`. They can be installed alongside upstream releases without deleting existing data. Their local trust database is independent.

Debug APKs use a development signing key and cannot update installations signed with the upstream release key. Older debug APKs built before this isolation change used the upstream application ID; do not uninstall an existing app just to try the new separate test build. The original `ruam-mij-1.3.2-debug.apk` was such a same-ID artifact; use `ruam-mij-1.3.2-test.apk` for side-by-side testing.

To record screenshots for visual review (quote the property argument in PowerShell):

```powershell
.\gradlew.bat :app:testDebugUnitTest '-Proborazzi.test.record=true'
```

Images are written to `app/build/outputs/roborazzi/`. Recording images alone does not compare them against a reviewed baseline.

## Manual checks for 1.3.1

1. Use two benign test apps installed from the same source. Trust one in Installed Apps. Its detail sheet and list should say it is trusted by you; the sibling and installer status must remain unchanged.
2. Keep custom sorting and installer/system-app filters selected while changing trust. They should be preserved, and the open detail sheet should update.
3. Reopen the app and verify trust persists. Remove trust, then revisit the overview and verify the app is included again if its installer is unverified.
4. With test fixtures, reinstall the same package signed with a different certificate, then rescan. The replacement must not inherit trust. An unreadable certificate must never grant trust.
5. Upgrade a locally signed version 1 database to this version using the same signing key. Other data should survive; old decisions without a certificate require confirmation again.
6. Repeat in English and Thai, with dark mode, a narrow screen and larger fonts. Confirm the trust/remove-trust controls and explanation remain readable.

User trust is a local preference, not a malware verdict. This patch deliberately leaves installer classification and screen-sharing compatibility as separate future work.
