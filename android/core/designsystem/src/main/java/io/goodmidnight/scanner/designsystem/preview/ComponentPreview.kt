package io.goodmidnight.scanner.designsystem.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.goodmidnight.scanner.designsystem.theme.Theme

/**
 * [ComponentPreview]
 * - [Composable] 컴포넌트를 보여주기 위한 Day/Night 멀티 테마 애노테이션
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Night", group = "Component", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day", group = "Component", uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class ComponentPreview

/**
 * [DesignSystemShowcaseAnnotationPreview]
 * - 대통합 Showcase를 스튜디오 프리뷰 패널에서 실시간 Day/Night 테마로 렌더링하기 위한 함수입니다.
 */
@ComponentPreview
@Composable
fun DesignSystemShowcaseAnnotationPreview() {
    Theme {
        DesignSystemShowcase()
    }
}
