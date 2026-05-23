package io.goodmidnight.scanner.designsystem.preview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.goodmidnight.scanner.designsystem.theme.Theme

/**
 * [ComponentPreview]
 * - Day/Night multi-theme annotation for previewing [Composable] components
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Night", group = "Component", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day", group = "Component", uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class ComponentPreview

/**
 * [DesignSystemShowcaseAnnotationPreview]
 * - Function for rendering the comprehensive Showcase in the Studio preview panel with live Day/Night themes.
 */
@ComponentPreview
@Composable
fun DesignSystemShowcaseAnnotationPreview() {
    Theme {
        DesignSystemShowcase()
    }
}
