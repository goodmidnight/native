package io.goodmidnight.scanner.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {
    val ArrowBack: ImageVector = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(20f, 12f)
        lineTo(4f, 12f)
        moveTo(11f, 5f)
        lineTo(4f, 12f)
        lineTo(11f, 19f)
    }.build()
    val ArrowBackIosNew: ImageVector = ImageVector.Builder(
        name = "ArrowBackIosNew",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(16f, 4f)
        lineTo(8f, 12f)
        lineTo(16f, 20f)
    }.build()
    val ArrowDropDown: ImageVector = ImageVector.Builder(
        name = "ArrowDropDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(7f, 10f)
        lineTo(12f, 15f)
        lineTo(17f, 10f)
    }.build()
    val ArrowDropUp: ImageVector = ImageVector.Builder(
        name = "ArrowDropUp",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(7f, 14f)
        lineTo(12f, 9f)
        lineTo(17f, 14f)
    }.build()

    val Clear: ImageVector = ImageVector.Builder(
        name = "Clear",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(18f, 6f)
        lineTo(6f, 18f)
        moveTo(6f, 6f)
        lineTo(18f, 18f)
    }.build()
    val Search: ImageVector = ImageVector.Builder(
        name = "Search",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(16f, 10.5f)
        curveTo(16f, 13.5376f, 13.5376f, 16f, 10.5f, 16f)
        curveTo(7.4624f, 16f, 5f, 13.5376f, 5f, 10.5f)
        curveTo(5f, 7.4624f, 7.4624f, 5f, 10.5f, 5f)
        curveTo(13.5376f, 5f, 16f, 7.4624f, 16f, 10.5f)
        close()
        moveTo(14.5f, 14.5f)
        lineTo(20f, 20f)
    }.build()

    val Delete: ImageVector = ImageVector.Builder(
        name = "Delete",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(5f, 7f)
        lineTo(6.5f, 20f)
        curveTo(6.5f, 21f, 7.5f, 21f, 8f, 21f)
        lineTo(16f, 21f)
        curveTo(16.5f, 21f, 17.5f, 21f, 17.5f, 20f)
        lineTo(19f, 7f)
        moveTo(3f, 7f)
        lineTo(21f, 7f)
        moveTo(9f, 7f)
        lineTo(9f, 4f)
        lineTo(15f, 4f)
        lineTo(15f, 7f)
    }.build()

    val Share: ImageVector = ImageVector.Builder(
        name = "Share",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(18f, 5f)
        curveTo(18f, 6.1f, 17.1f, 7f, 16f, 7f)
        curveTo(14.9f, 7f, 14f, 6.1f, 14f, 5f)
        curveTo(14f, 3.9f, 14.9f, 3f, 16f, 3f)
        curveTo(17.1f, 3f, 18f, 3.9f, 18f, 5f)
        close()
        moveTo(18f, 19f)
        curveTo(18f, 20.1f, 17.1f, 21f, 16f, 21f)
        curveTo(14.9f, 21f, 14f, 20.1f, 14f, 19f)
        curveTo(14f, 17.9f, 14.9f, 17f, 16f, 17f)
        curveTo(17.1f, 17f, 18f, 17.9f, 18f, 19f)
        close()
        moveTo(8f, 12f)
        curveTo(8f, 13.1f, 7.1f, 14f, 6f, 14f)
        curveTo(4.9f, 14f, 4f, 13.1f, 4f, 12f)
        curveTo(4f, 10.9f, 4.9f, 10f, 6f, 10f)
        curveTo(7.1f, 10f, 8f, 10.9f, 8f, 12f)
        close()
        moveTo(8f, 11f)
        lineTo(14f, 6.5f)
        moveTo(8f, 13f)
        lineTo(14f, 17.5f)
    }.build()

    val Folder: ImageVector = ImageVector.Builder(
        name = "Folder",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 6f)
        lineTo(8f, 6f)
        lineTo(10f, 9f)
        lineTo(20f, 9f)
        lineTo(20f, 19f)
        curveTo(20f, 20f, 19f, 20f, 18f, 20f)
        lineTo(6f, 20f)
        curveTo(5f, 20f, 4f, 20f, 4f, 19f)
        close()
    }.build()

    val SaveAlt: ImageVector = ImageVector.Builder(
        name = "SaveAlt",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 4f)
        lineTo(12f, 15f)
        moveTo(8f, 11f)
        lineTo(12f, 15f)
        lineTo(16f, 11f)
        moveTo(4f, 17f)
        lineTo(4f, 20f)
        lineTo(20f, 20f)
        lineTo(20f, 17f)
    }.build()

    val FlashOn: ImageVector = ImageVector.Builder(
        name = "FlashOn",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(7f, 13f)
        lineTo(12f, 13f)
        lineTo(10f, 22f)
        lineTo(17f, 11f)
        lineTo(12f, 11f)
        close()
    }.build()

    val FlashOff: ImageVector = ImageVector.Builder(
        name = "FlashOff",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(9.5f, 6.5f)
        moveTo(8.5f, 9f)
        lineTo(7f, 13f)
        lineTo(12f, 13f)
        lineTo(11f, 17.5f)
        moveTo(11.5f, 19.5f)
        lineTo(10f, 22f)
        lineTo(14.5f, 15f)
        moveTo(16f, 12.5f)
        lineTo(17f, 11f)
        lineTo(12f, 11f)
        lineTo(12f, 8.5f)
        moveTo(4f, 4f)
        lineTo(20f, 20f)
    }.build()

    val Grid3x3: ImageVector = ImageVector.Builder(
        name = "Grid3x3",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(2f, 8f)
        lineTo(22f, 8f)
        moveTo(2f, 16f)
        lineTo(22f, 16f)
        moveTo(8f, 2f)
        lineTo(8f, 22f)
        moveTo(16f, 2f)
        lineTo(16f, 22f)
    }.build()

    val Check: ImageVector = ImageVector.Builder(
        name = "Check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 12f)
        lineTo(9f, 17f)
        lineTo(20f, 6f)
    }.build()

    val Add: ImageVector = ImageVector.Builder(
        name = "Add",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 5f)
        lineTo(12f, 19f)
        moveTo(5f, 12f)
        lineTo(19f, 12f)
    }.build()

    val Remove: ImageVector = ImageVector.Builder(
        name = "Remove",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(5f, 12f)
        lineTo(19f, 12f)
    }.build()

    val KeyboardArrowDown: ImageVector = ImageVector.Builder(
        name = "KeyboardArrowDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(6f, 9f)
        lineTo(12f, 15f)
        lineTo(18f, 9f)
    }.build()

    val Menu: ImageVector = ImageVector.Builder(
        name = "Menu",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 6f)
        lineTo(20f, 6f)
        moveTo(4f, 12f)
        lineTo(20f, 12f)
        moveTo(4f, 18f)
        lineTo(20f, 18f)
    }.build()

    val Home: ImageVector = ImageVector.Builder(
        name = "Home",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(3f, 11f)
        lineTo(12f, 3f)
        lineTo(21f, 11f)
        moveTo(5f, 11f)
        lineTo(5f, 20f)
        curveTo(5f, 21f, 6f, 21f, 6f, 21f)
        lineTo(18f, 21f)
        curveTo(18f, 21f, 19f, 21f, 19f, 20f)
        lineTo(19f, 11f)
        moveTo(10f, 21f)
        lineTo(10f, 15f)
        lineTo(14f, 15f)
        lineTo(14f, 21f)
    }.build()

    val Settings: ImageVector = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 9f)
        curveTo(10.3f, 9f, 9f, 10.3f, 9f, 12f)
        curveTo(9f, 13.7f, 10.3f, 15f, 12f, 15f)
        curveTo(13.7f, 15f, 15f, 13.7f, 15f, 12f)
        curveTo(15f, 10.3f, 13.7f, 9f, 12f, 9f)
        close()
        moveTo(19.4f, 13f)
        curveTo(19.5f, 12.7f, 19.5f, 12.3f, 19.5f, 12f)
        curveTo(19.5f, 11.7f, 19.5f, 11.3f, 19.4f, 11f)
        lineTo(21.4f, 9.5f)
        lineTo(19.5f, 6.2f)
        lineTo(17.1f, 7.1f)
        curveTo(16.6f, 6.7f, 16.1f, 6.4f, 15.5f, 6.2f)
        lineTo(15.1f, 3.6f)
        lineTo(11.3f, 3.6f)
        lineTo(10.9f, 6.2f)
        curveTo(10.3f, 6.4f, 9.8f, 6.7f, 9.3f, 7.1f)
        lineTo(6.9f, 6.2f)
        lineTo(5f, 9.5f)
        lineTo(7f, 11f)
        curveTo(6.9f, 11.3f, 6.9f, 11.7f, 6.9f, 12f)
        curveTo(6.9f, 12.3f, 6.9f, 12.7f, 7f, 13f)
        lineTo(5f, 14.5f)
        lineTo(6.9f, 17.8f)
        lineTo(9.3f, 16.9f)
        curveTo(9.8f, 17.3f, 10.3f, 17.6f, 10.9f, 17.8f)
        lineTo(11.3f, 20.4f)
        lineTo(15.1f, 20.4f)
        lineTo(15.5f, 17.8f)
        curveTo(16.1f, 17.6f, 16.6f, 17.3f, 17.1f, 16.9f)
        lineTo(19.5f, 17.8f)
        lineTo(21.4f, 14.5f)
        lineTo(19.4f, 13f)
        close()
    }.build()

    val Library: ImageVector = ImageVector.Builder(
        name = "Library",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(8f, 3f)
        lineTo(18f, 3f)
        curveTo(19.1f, 3f, 20f, 3.9f, 20f, 5f)
        lineTo(20f, 15f)
        curveTo(20f, 16.1f, 19.1f, 17f, 18f, 17f)
        moveTo(4f, 7f)
        lineTo(14f, 7f)
        curveTo(15.1f, 7f, 16f, 7.9f, 16f, 9f)
        lineTo(16f, 19f)
        curveTo(16f, 20.1f, 15.1f, 21f, 14f, 21f)
        lineTo(4f, 21f)
        curveTo(2.9f, 21f, 2f, 20.1f, 2f, 19f)
        lineTo(2f, 9f)
        curveTo(2f, 7.9f, 2.9f, 7.0f, 4f, 7f)
        close()
        moveTo(6f, 11f)
        lineTo(12f, 11f)
        moveTo(6f, 15f)
        lineTo(10f, 15f)
    }.build()

    val ArrowForward: ImageVector = ImageVector.Builder(
        name = "ArrowForward",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(4f, 12f)
        lineTo(20f, 12f)
        moveTo(13f, 5f)
        lineTo(20f, 12f)
        lineTo(13f, 19f)
    }.build()

    val KeyboardArrowRight: ImageVector = ImageVector.Builder(
        name = "KeyboardArrowRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(9f, 6f)
        lineTo(15f, 12f)
        lineTo(9f, 18f)
    }.build()

    val Info: ImageVector = ImageVector.Builder(
        name = "Info",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        curveTo(17.5f, 2f, 22f, 6.5f, 22f, 12f)
        curveTo(22f, 17.5f, 17.5f, 22f, 12f, 22f)
        curveTo(6.5f, 22f, 2f, 17.5f, 2f, 12f)
        curveTo(2f, 6.5f, 6.5f, 2f, 12f, 2f)
        close()
        moveTo(12f, 8f)
        lineTo(12f, 8.1f)
        moveTo(12f, 12f)
        lineTo(12f, 17f)
    }.build()

    val Warning: ImageVector = ImageVector.Builder(
        name = "Warning",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color(0xFF212529)),
        strokeLineWidth = 2.2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 3f)
        lineTo(2f, 21f)
        lineTo(22f, 21f)
        close()
        moveTo(12f, 9f)
        lineTo(12f, 14f)
        moveTo(12f, 18f)
        lineTo(12f, 18.1f)
    }.build()
}
