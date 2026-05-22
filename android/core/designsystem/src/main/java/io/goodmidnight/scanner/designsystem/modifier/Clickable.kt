package io.goodmidnight.scanner.designsystem.modifier

import android.annotation.SuppressLint
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * [throttleClickable]
 * - 클릭 인터렉션이 연속적으로 발생하는 것을 방지하는 확장자
 *
 * @param enabled 클릭 인터렉션이 활성화된 상태
 * @param onClickLabel 클릭 인터렉션의 레이블
 * @param role 클릭 인터렉션의 역할
 * @param coroutineContext 코루틴 context
 * @param throttleTime 클릭 인터렉션의 딜레이 시간
 * @param onClick 클릭 인터렉션 콜백
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
                coroutineScope.launch {
                    lastEmissionTime = currentTime
                    onClick()
                }
            }
            lastEmissionTime = currentTime
        },
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
    )
}

/**
 * [throttleClickable]
 * - 클릭 인터렉션이 연속적으로 발생하는 것을 방지하는 확장자
 *
 * @param interactionSource 클릭 인터렉션의 인터렉션 소스
 * @param indication 클릭 인터렉션의 인디케이션
 * @param enabled 클릭 인터렉션이 활성화된 상태
 * @param onClickLabel 클릭 인터렉션의 레이블
 * @param role 클릭 인터렉션의 역할
 * @param coroutineContext 코루틴 context
 * @param throttleTime 클릭 인터렉션의 딜레이 시간
 * @param onClick 클릭 인터렉션 콜백
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
                coroutineScope.launch {
                    lastEmissionTime = currentTime
                    onClick()
                }
            }
            lastEmissionTime = currentTime
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
