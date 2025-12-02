package com.akexorcist.ruammij.functional.mediaprojection

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class MediaProjectionEventManager {
    private val _mediaProjectionEvent = Channel<AutoMediaProjectionDetectionEvent?>()
    val mediaProjectionEvent = _mediaProjectionEvent.receiveAsFlow()

    suspend fun onMediaProjectionDetected(
        packageName: String,
        displayId: Int,
    ) {
        _mediaProjectionEvent.send(
            AutoMediaProjectionDetectionEvent.Detected(
                packageName = packageName,
                displayId = displayId,
            )
        )
    }

    suspend fun onMediaProjectionDeactivated(
        displayId: Int,
    ) {
        _mediaProjectionEvent.send(
            AutoMediaProjectionDetectionEvent.Deactivated(
                displayId = displayId,
            )
        )
    }
}

sealed class AutoMediaProjectionDetectionEvent {
    data class Detected(
        val packageName: String,
        val displayId: Int,
    ) : AutoMediaProjectionDetectionEvent()

    data class Deactivated(
        val displayId: Int,
    ) : AutoMediaProjectionDetectionEvent()
}
