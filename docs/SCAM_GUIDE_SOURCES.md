# Scam-prevention guide: sources and maintenance

Reviewed on 25 October 2026. The guide is an editorial summary in Thai and English, bundled with the APK; it is not a live threat feed, a malware scanner, or a claim that a specific installed app is malicious.

Users open it from **About App → Scam prevention and help**. Reading requires no internet. Reference buttons open the external browser. The AOC button uses `ACTION_DIAL` for `tel:1441`; it does not place a call or require CALL_PHONE permission. Devices without a suitable handler display a message.

## Evidence map

| Topic | Primary source | Application in the guide |
|---|---|---|
| Thai incident response | [MDES: AOC 1441](https://mdes.go.th/mission/153/) | Contact the bank and AOC through official channels; preserve evidence and follow reporting instructions. Do not promise recovery or a fixed response deadline. |
| QR and phishing | [FTC: QR scams, September 2026](https://consumer.ftc.gov/consumer-alerts/2026/09/see-qr-code-parked-somewhere-dont-scan-ityet), [FTC: QR links](https://consumer.ftc.gov/consumer-alerts/2023/12/scammers-hide-harmful-links-qr-codes-steal-your-information) | Inspect destinations, independently verify the service, and secure accounts after entering credentials. Checking the payment recipient is an additional practical precaution. |
| Task and investment-style manipulation | [FTC: task scams, August 2025](https://consumer.ftc.gov/consumer-alerts/2025/08/how-spot-avoid-task-scams) | Early payouts and apparent earnings do not justify paying to unlock work or withdrawals. |
| Repeat victimization | [FTC: recovery scams, August 2026](https://consumer.ftc.gov/consumer-alerts/2026/08/have-you-lost-money-scam-watch-scammers-who-say-they-can-help) | Do not pay unsolicited recovery offers. |
| Account exposure | [FTC: what to do after a scam](https://consumer.ftc.gov/articles/what-do-if-you-were-scammed) | Contact the payment provider promptly; change exposed/reused passwords, enable MFA and use trusted support. US-specific legal rights and US reporting numbers are not presented as Thai procedures. |
| Android protections | [Google: Play Protect](https://support.google.com/android/answer/2812853?hl=en) | Keep scanning enabled; explain that some Google scanning options transmit app information separately from Ruam Mij. |
| AI impersonation and new call protection | [Google: fake call detection, June 2026](https://blog.google/security/android-fake-call-detection/) | Voices and caller ID can be impersonated. Describe platform prerequisites and avoid claiming universal or Thai availability. Independent callback and a family verification phrase are practical editorial recommendations, not a detection guarantee. |
| Scan limitations | Repository `Display.kt`, `DeviceRepository.kt`, `PackageManager.kt` and [Android non-SDK restrictions](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces) | A missing screen-sharing result does not prove absence. Installer recognition, requested permissions and user trust each have a limited meaning. |

## Review procedure

Before the next content release, reopen these primary sources and verify dates, phone numbers, target country, Android prerequisites and availability. Update both `values/safety_guide.xml` and `values-th/safety_guide.xml`, their review-date labels and this file together. Reword outdated facts rather than simply replacing the date.

Keep incident response short and visible first. Never add guarantees of safety or recovery, automatic background monitoring claims, a paid recovery service, or instructions to disable Android protections to use this app. Review privacy text whenever local storage, external actions, permissions or networking changes.

Run the full unit suite and lint, record the guide in both languages, and inspect narrow-screen output. Screenshot recording is not automatic pixel-baseline verification. A physical-device check is still needed before a public APK release.
