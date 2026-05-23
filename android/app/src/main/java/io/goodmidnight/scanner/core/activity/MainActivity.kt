package io.goodmidnight.scanner.core.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.goodmidnight.scanner.designsystem.theme.ColorTheme
import io.goodmidnight.scanner.ui.core.component.MainApp
import io.goodmidnight.scanner.designsystem.theme.Theme
import io.goodmidnight.scanner.model.Theme.DARK
import io.goodmidnight.scanner.model.Theme.LIGHT
import io.goodmidnight.scanner.model.Theme.SYSTEM

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: ActivityViewModel by viewModels()

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            val context = LocalContext.current
            var hasCameraPermission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasCameraPermission = isGranted
                }
            )
            LaunchedEffect(Unit) {
                if (!hasCameraPermission) {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            Theme(theme = when(state.theme){
                LIGHT -> ColorTheme.LIGHT
                DARK -> ColorTheme.DARK
                SYSTEM -> ColorTheme.SYSTEM
            }) {
                MainApp(navController = navController)
            }
        }
    }
}
