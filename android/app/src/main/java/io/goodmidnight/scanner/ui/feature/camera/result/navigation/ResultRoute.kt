package io.goodmidnight.scanner.ui.feature.camera.result.navigation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.goodmidnight.scanner.ui.core.utils.LocalSnackbarHostState
import io.goodmidnight.scanner.ui.core.utils.showSnackbarImmediately
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultEffect
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultEvent
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultState
import io.goodmidnight.scanner.ui.feature.camera.result.data.ResultViewModel
import io.goodmidnight.scanner.ui.feature.camera.result.ui.ResultScreen
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedEvent
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState
import io.goodmidnight.scanner.ui.feature.camera.shared.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun ResultRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state: ResultState by viewModel.state.collectAsStateWithLifecycle()
    val sharedState: SharedState by sharedViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png")
    ) { uri ->
        uri?.let {
            coroutineScope.launch(Dispatchers.IO) {
                val bitmap = sharedState.captureResult?.image ?: return@launch
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                withContext(Dispatchers.Main) {
                    snackbarHostState.showSnackbarImmediately(coroutineScope, "이미지가 저장되었습니다.")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.bindEffect(scope = this) { effect ->
            when (effect) {
                is ResultEffect.ShowSnackBar -> snackbarHostState.showSnackbarImmediately(
                    coroutineScope,
                    effect.message
                )

                ResultEffect.PopBackStack -> {
                    sharedViewModel.onEvent(SharedEvent.OnUpdateStep(SharedState.ScannerStep.PREVIEW))
                    popBackStack()
                }

                is ResultEffect.ShareImage -> {
                    shareBitmap(context, effect.bitmap)
                }

                is ResultEffect.SaveToFolder -> {
                    saveLauncher.launch("scanned_${System.currentTimeMillis()}.png")
                }
            }
        }
    }

    BackHandler(onBack = remember { { viewModel.onEvent(ResultEvent.OnBack) } })

    ResultScreen(
        modifier = modifier,
        state = state,
        sharedState = sharedState,
        onBack = remember { { viewModel.onEvent(ResultEvent.OnBack) } },
        onSave = remember { { bitmap -> viewModel.onEvent(ResultEvent.OnSaveToFolder(bitmap)) } },
        onShare = remember { { bitmap -> viewModel.onEvent(ResultEvent.OnShare(bitmap)) } },
        onTextCopy = remember { { viewModel.onEvent(ResultEvent.OnTextCopy) } }
    )
}

private fun shareBitmap(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Share").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        context.startActivity(chooser)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
