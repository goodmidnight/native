package io.goodmidnight.scanner.model

data class DocumentFrame(
    val points: FloatArray, // [x1, y1, x2, y2, x3, y3, x4, y4]
    val isDetected: Boolean,
    val confidence: Float,
    val isStable: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentFrame

        if (isDetected != other.isDetected) return false
        if (confidence != other.confidence) return false
        if (isStable != other.isStable) return false
        if (!points.contentEquals(other.points)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isDetected.hashCode()
        result = 31 * result + confidence.hashCode()
        result = 31 * result + isStable.hashCode()
        result = 31 * result + points.contentHashCode()
        return result
    }
}