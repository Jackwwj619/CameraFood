package com.caloriesnap.presentation.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.caloriesnap.presentation.navigation.Screen
import java.io.File
import java.net.URLEncoder
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController: NavController, mealType: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { hasCameraPermission = it }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { navController.navigate(Screen.Recognition.createRoute(URLEncoder.encode(it.toString(), "UTF-8"), mealType)) }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("拍照识别") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "返回") }
        })
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (hasCameraPermission) {
                AndroidView(factory = { ctx ->
                    PreviewView(ctx).apply {
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also { it.setSurfaceProvider(surfaceProvider) }
                            imageCapture = ImageCapture.Builder().build()
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                            } catch (_: Exception) {}
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                }, Modifier.fillMaxSize())

                Row(Modifier.align(Alignment.BottomCenter).padding(32.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    FloatingActionButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(Icons.Default.PhotoLibrary, "相册")
                    }
                    FloatingActionButton(onClick = {
                        takePhoto(context, imageCapture) { uri ->
                            navController.navigate(Screen.Recognition.createRoute(URLEncoder.encode(uri.toString(), "UTF-8"), mealType))
                        }
                    }, containerColor = MaterialTheme.colorScheme.primary) {
                        Icon(Icons.Default.Camera, "拍照")
                    }
                }
            } else {
                Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("需要相机权限")
                    Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) { Text("授权") }
                }
            }
        }
    }
}

private fun takePhoto(context: Context, imageCapture: ImageCapture?, onSuccess: (Uri) -> Unit) {
    val file = File(context.cacheDir, "images").apply { mkdirs() }.let { File(it, "${System.currentTimeMillis()}.jpg") }
    imageCapture?.takePicture(
        ImageCapture.OutputFileOptions.Builder(file).build(),
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) { onSuccess(Uri.fromFile(file)) }
            override fun onError(e: ImageCaptureException) {}
        }
    )
}
