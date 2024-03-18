package com.akexorcist.ruammij

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.data.InstalledApp
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SharedEventViewModel : ViewModel() {
    private val _mediaProjectionEvent = Channel<AutoMediaProjectionDetectionEvent?>()
    val mediaProjectionEvent = _mediaProjectionEvent.receiveAsFlow()

    fun onMediaProjectionDetected(
        app: InstalledApp,
        displayId: Int,
    ) = viewModelScope.launch {
        _mediaProjectionEvent.send(
            AutoMediaProjectionDetectionEvent.Detected(
                app = app,
                displayId = displayId,
            )
        )
    }

    fun onMediaProjectionDeactivated(
        displayId: Int,
    ) = viewModelScope.launch {
        _mediaProjectionEvent.send(
            AutoMediaProjectionDetectionEvent.Deactivated(
                displayId = displayId,
            )
        )
    }
}

sealed class AutoMediaProjectionDetectionEvent {
    data class Detected(
        val app: InstalledApp,
        val displayId: Int,
    ) : AutoMediaProjectionDetectionEvent()

    data class Deactivated(
        val displayId: Int,
    ) : AutoMediaProjectionDetectionEvent()
}
