package io.goodmidnight.scanner.model

data class ScannerConfig(
    var targetWidth: Int = 800,
    var cannySigma: Float = 0.33f,
    var minAreaRatio: Double = 0.15,
    var lowLightMode: Boolean = false
)