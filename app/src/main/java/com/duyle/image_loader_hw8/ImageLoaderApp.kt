package com.duyle.image_loader_hw8

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageLoaderApp(viewModel: ImageViewModel) {
    var url by remember { mutableStateOf("") }
    val imageBitmap = viewModel.imageBitmap.collectAsState()
    val status = viewModel.status.collectAsState()
    val isConnected = viewModel.isConnected.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.loadImage(url) },
            enabled = isConnected.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Image")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(status.value)

        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap.value?.let {
            Image(bitmap = it, contentDescription = null, modifier = Modifier.fillMaxWidth())
        }
    }
}
