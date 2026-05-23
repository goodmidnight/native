package io.goodmidnight.scanner.ui.feature.camera.crop.data

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState

data class CropEditState(
    val originalImage: Bitmap? = null,
    val vertexPoints: List<Offset> = emptyList(),
    val initialPoints: List<Offset> = emptyList(),
    val activePinIndex: Int? = null,
    val dragOffset: Offset = Offset.Zero,
    val isSnapped: Boolean = false,
    val selectedFilter: Int = 0, // 0: ORIGINAL, 1: GRAYSCALE, 2: B&W
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.INITIAL,
) : BaseState
