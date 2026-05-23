package io.goodmidnight.scanner.ui.feature.camera.crop.data

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.goodmidnight.scanner.ui.core.exception.AppError
import io.goodmidnight.scanner.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

import io.goodmidnight.scanner.ui.feature.camera.shared.SharedState

@HiltViewModel
class CropEditViewModel @Inject constructor() : BaseViewModel<CropEditState, CropEditEvent, CropEditEffect, AppError>(
    CropEditState()
) {
    private var initialVertexPoints: List<Offset> = emptyList()
    private var previewWidth: Int = 0
    private var previewHeight: Int = 0

    init {
        bindEvent { event ->
            when (event) {
                is CropEditEvent.OnInitialize -> {
                    val bitmap = event.image
                    val frame = event.detectedFrame
                    val points = mutableListOf<Offset>()
                    
                    if (frame != null) {
                        val previewW = frame.imageWidth
                        val previewH = frame.imageHeight
                        val capturedW = bitmap.width
                        val capturedH = bitmap.height

                        val scaleX = if (previewW > 0) capturedW.toFloat() / previewW.toFloat() else 1f
                        val scaleY = if (previewH > 0) capturedH.toFloat() / previewH.toFloat() else 1f

                        val rawPts = frame.points
                        for (i in 0 until 4) {
                            val xIndex = i * 2
                            val yIndex = i * 2 + 1
                            if (xIndex < rawPts.size && yIndex < rawPts.size) {
                                points.add(
                                    Offset(
                                        rawPts[xIndex] * scaleX,
                                        rawPts[yIndex] * scaleY
                                    )
                                )
                            }
                        }
                    }

                    if (points.size < 4) {
                        points.clear()
                        val w = bitmap.width.toFloat()
                        val h = bitmap.height.toFloat()
                        points.add(Offset(w * 0.1f, h * 0.1f))
                        points.add(Offset(w * 0.9f, h * 0.1f))
                        points.add(Offset(w * 0.9f, h * 0.9f))
                        points.add(Offset(w * 0.1f, h * 0.9f))
                    }

                    initialVertexPoints = points.toList()

                    updateState {
                        copy(
                            originalImage = bitmap,
                            vertexPoints = points,
                            initialPoints = points,
                            selectedFilter = 0
                        )
                    }
                }

                is CropEditEvent.OnPinTouchDown -> {
                    updateState { copy(activePinIndex = event.index) }
                    emitEffect(CropEditEffect.TriggerLongPressHaptic)
                }

                is CropEditEvent.OnPinDrag -> {
                    handlePinDrag(event.index, event.newPosition)
                }

                is CropEditEvent.OnPinTouchUp -> {
                    updateState {
                        copy(
                            activePinIndex = null,
                            isSnapped = false
                        )
                    }
                }

                is CropEditEvent.OnSelectFilter -> {
                    updateState { copy(selectedFilter = event.filterIndex) }
                }

                is CropEditEvent.OnCompleteCrop -> {
                    val points = event.points
                    if (points.size == 4) {
                        val floatPts = FloatArray(8)
                        for (i in 0 until 4) {
                            floatPts[i * 2] = points[i].x
                            floatPts[i * 2 + 1] = points[i].y
                        }
                        emitEffect(
                            CropEditEffect.NavigateToResult(
                                points = floatPts,
                                filterIndex = state.value.selectedFilter
                            )
                        )
                    }
                }

                is CropEditEvent.OnBack -> {
                    emitEffect(CropEditEffect.PopBackStack)
                }
            }
        }
    }

    private fun handlePinDrag(index: Int, rawNewPos: Offset) {
        val originalPos = initialVertexPoints.getOrNull(index)
        var finalPos = rawNewPos
        var snapTriggered = false

        if (originalPos != null) {
            val dist = distance(rawNewPos, originalPos)
            val currentSnapped = state.value.isSnapped

            // Magnetic snap thresholds:
            // 24px limit for triggering snap
            // 45px limit for releasing snap
            if (dist < 24f) {
                finalPos = originalPos
                snapTriggered = true
                if (!currentSnapped) {
                    viewModelScope.launch {
                        emitEffect(CropEditEffect.TriggerSnapHaptic)
                    }
                }
            } else if (dist < 45f && currentSnapped) {
                // Keep snapped until user drags further
                finalPos = originalPos
                snapTriggered = true
            }
        }

        updateState {
            val newPoints = vertexPoints.toMutableList()
            if (index in newPoints.indices) {
                newPoints[index] = finalPos
            }
            copy(
                vertexPoints = newPoints.toList(),
                isSnapped = snapTriggered,
                dragOffset = rawNewPos
            )
        }
    }

    private fun distance(p1: Offset, p2: Offset): Float {
        val dx = p1.x - p2.x
        val dy = p1.y - p2.y
        return sqrt(dx * dx + dy * dy)
    }
}
