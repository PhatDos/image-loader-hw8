package com.duyle.image_loader_hw8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor() : ViewModel() {

    private val _imageBitmap = MutableStateFlow<ImageBitmap?>(null)
    val imageBitmap: StateFlow<ImageBitmap?> = _imageBitmap

    private val _status = MutableStateFlow("Enter URL and click Load Image")
    val status: StateFlow<String> = _status

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun setNetworkStatus(connected: Boolean) {
        _isConnected.value = connected
        if (!connected) {
            _status.value = "No internet connection"
        }
    }

    fun loadImage(urlString: String) {
        if (!_isConnected.value) {
            _status.value = "No internet connection"
            return
        }

        // Make sure to launch the coroutine on IO Dispatcher
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = "Loading..."
            val result = loadBitmapFromUrl(urlString)

            // Switch back to the main thread to update UI
            launch(Dispatchers.Main) {
                result.onSuccess { bitmap ->
                    _imageBitmap.value = bitmap.asImageBitmap()
                    _status.value = "Image loaded!"
                }.onFailure { exception ->
                    _status.value = "Failed to load image: ${exception.localizedMessage}"
                }
            }
        }
    }

    private fun loadBitmapFromUrl(urlString: String): Result<Bitmap> {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connect()
            val input = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(input)
            Result.success(bitmap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
