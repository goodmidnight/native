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
 * - 클릭 가능한 요소에 눌렸을 때 크기가 살짝 줄어드는(scale: 0.96) 부드러운 피드백 애니메이션을 적용합니다.
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
                    indication = null, // 리플 비활성화하고 바운스 애니메이션만 깔끔하게 전달
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
 * - 강한 실선을 지양하고, 우버 디자인 시스템 고유의 아주 은은하고 넓게 퍼지는 그림자를 렌더링합니다.
 * - 예시: color: #000, opacity: 0.03, blur: 20px
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

        // 하드웨어 가속 섀도우 적용 (shadowRadius, dx, dy, color)
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
 * - 클릭 인터렉션이 연속적으로 발생하는 것을 방지하는 확장자
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
 * - 클릭 인터렉션이 연속적으로 발생하는 것을 방지하는 확장자 (interactionSource 지정)
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
 * 클릭 인터렉션 중 기본적으로 발생하는 시각 이펙트를 제거하는 확장자
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
