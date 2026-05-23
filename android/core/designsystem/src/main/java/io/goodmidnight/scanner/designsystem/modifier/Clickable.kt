package io.goodmidnight.scanner.designsystem.modifier

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * [bounceClick]
 * - Applies a soft bounce feedback animation that slightly scales down (0.96) clickable elements when pressed.
 */
fun Modifier.bounceClick(
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1.0f,
        label = "BounceScaleAnimation"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .composed {
            if (onClick != null) {
                this.throttleClickable(
                    interactionSource = interactionSource,
                    indication = null, // Disable ripple; deliver only the clean bounce animation
                    enabled = enabled,
                    onClick = onClick
                )
            } else {
                this
            }
        }
}

/**
 * [softShadow]
 * - Avoids hard outlines; renders the subtle, wide-spreading shadow characteristic of the design system.
 * - Example: color: #000, opacity: 0.03, blur: 20px
 */
fun Modifier.softShadow(
    color: Color = Color.Black,
    alpha: Float = 0.03f,
    borderRadius: Dp = 24.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    this.drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor

        // Apply hardware-accelerated shadow (shadowRadius, dx, dy, color)
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )

        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}

/**
 * [throttleClickable]
 * - Extension that prevents rapid successive click interactions
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.throttleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    coroutineContext: CoroutineContext = Dispatchers.Main,
    throttleTime: Long = 250L,
    onClick: () -> Unit,
) = composed {
    val coroutineScope = rememberCoroutineScope { coroutineContext }
    var lastEmissionTime: Long by remember { mutableLongStateOf(0L) }

    clickable(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime >= throttleTime) {
                lastEmissionTime = currentTime
                coroutineScope.launch {
                    onClick()
                }
            }
        },
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
    )
}

/**
 * [throttleClickable]
 * - Extension that prevents rapid successive click interactions (with interactionSource)
 */
fun Modifier.throttleClickable(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    coroutineContext: CoroutineContext = Dispatchers.Main,
    throttleTime: Long = 250L,
    onClick: () -> Unit,
) = composed {
    val coroutineScope = rememberCoroutineScope { coroutineContext }
    var lastEmissionTime: Long by remember { mutableLongStateOf(0L) }

    noRippleClickable(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime >= throttleTime) {
                lastEmissionTime = currentTime
                coroutineScope.launch {
                    onClick()
                }
            }
        },
        interactionSource = remember { interactionSource },
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role
    )
}

/**
 * Extension that removes default visual effects during click interactions
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed {
    clickable(
        interactionSource = remember { interactionSource },
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick,
        role = role
    )
}
