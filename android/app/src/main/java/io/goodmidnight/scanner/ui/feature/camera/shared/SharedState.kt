package io.goodmidnight.scanner.ui.feature.camera.shared

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import io.goodmidnight.scanner.model.CaptureResult
import io.goodmidnight.scanner.model.DocumentFrame
import io.goodmidnight.scanner.model.ImageQuality
import io.goodmidnight.scanner.model.OcrBlock
import io.goodmidnight.scanner.model.OcrLanguage
import io.goodmidnight.scanner.ui.core.viewmodel.BaseState
import io.goodmidnight.scanner.ui.core.viewmodel.ScreenState
import io.goodmidnight.scanner.ui.core.viewmodel.UiState

@Immutable
data class SharedState(
    override val uiState: UiState = UiState(),
    override val screenState: ScreenState = ScreenState.INITIAL,

    // Settings
    val imageQuality: ImageQuality = ImageQuality.MEDIUM,
    val autoSaveToGallery: Boolean = false,
    val showGridLines: Boolean = false,

    // Scanner State
    val detectedFrame: DocumentFrameState? = null,
    val captureResult: CaptureResultState? = null,
    val documentType: DocumentType = DocumentType.GENERAL,
    val captureMode: CaptureMode = CaptureMode.SCAN,
    val ocrLanguage: OcrLanguage = OcrLanguage.KOREAN,
    val currentStep: ScannerStep = ScannerStep.PREVIEW,
    val rawCapturedBitmap: Bitmap? = null,
    val rawCapturedRotation: Int = 0,
    val rawCapturedProcessingMode: Int = 0,
) : BaseState {

    enum class ScannerStep {
        PREVIEW,
        CAPTURING,
        CROP_EDIT,
        RESULT
    }

    data class DocumentFrameState(
        val points: FloatArray,
        val isDetected: Boolean,
        val confidence: Float,
        val isStable: Boolean,
        val imageWidth: Int = 0,
        val imageHeight: Int = 0,
    ) {
        constructor(domain: DocumentFrame, bitmap: Bitmap) : this(
            points = domain.points,
            isDetected = domain.isDetected,
            confidence = domain.confidence,
            isStable = domain.isStable,
            imageWidth = bitmap.width,
            imageHeight = bitmap.height
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DocumentFrameState

            if (!points.contentEquals(other.points)) return false
            if (isDetected != other.isDetected) return false
            if (confidence != other.confidence) return false
            if (isStable != other.isStable) return false

            return true
        }

        override fun hashCode(): Int {
            var result = points.contentHashCode()
            result = 31 * result + isDetected.hashCode()
            result = 31 * result + confidence.hashCode()
            result = 31 * result + isStable.hashCode()
            return result
        }
    }

    /**
     * Represents the UI state of a document capture result.
     * This class holds the processed image and metadata regarding the quality of the capture.
     * @param image The cropped and perspective-corrected document image. Null if capture failed.
     * @param status The current status of the capture process (e.g., SUCCESS, FAILED).
     * @param isBlurry True if the captured image is detected as blurred or out of focus.
     * @param hasGlare True if severe light reflection or glare is detected on the document surface.
     * @param message A descriptive message regarding the capture result or error details.
     */
    data class CaptureResultState(
        val image: Bitmap?,
        val status: CaptureStatus,
        val isBlurry: Boolean,
        val hasGlare: Boolean,
        val message: String,
        val ocrBlocks: List<OcrBlock> = emptyList(),
        val fullText: String = "",
    ) {
        /**
         * Secondary constructor used to map a Domain layer [CaptureResult] model
         * to this Presentation layer [CaptureResultState] model.
         * * @param domain The raw capture result data received from the core engine.
         */
        constructor(model: CaptureResult) : this(
            image = model.image,
            status = CaptureStatus.fromCode(model.status),
            isBlurry = model.isBlurry,
            hasGlare = model.hasGlare,
            message = model.message,
        )

        constructor(model: CaptureResult, ocrBlocks: List<OcrBlock>, fullText: String) : this(
            image = model.image,
            status = CaptureStatus.fromCode(model.status),
            isBlurry = model.isBlurry,
            hasGlare = model.hasGlare,
            message = model.message,
            ocrBlocks = ocrBlocks,
            fullText = fullText
        )
    }

    enum class DocumentType(val code: Int, val displayName: String) {
        GENERAL(0, "General"),
        ID_CARD(1, "ID Card"),
        BUSINESS_CARD(2, "Business Card"),
        RECEIPT(3, "Receipt")
    }

    enum class CaptureMode(val code: Int, val displayName: String) {
        SCAN(0, "Scan"),
        OCR(1, "Text")
    }

    enum class CaptureStatus(val code: Int) {
        SUCCESS(0),
        ERR_NOT_DETECTED(1),
        ERR_BLURRY(2),
        ERR_GLARE(3),
        ERR_UNKNOWN(4);

        companion object {
            fun fromCode(code: Int): CaptureStatus = entries.find { it.code == code } ?: ERR_UNKNOWN
        }
    }
}
