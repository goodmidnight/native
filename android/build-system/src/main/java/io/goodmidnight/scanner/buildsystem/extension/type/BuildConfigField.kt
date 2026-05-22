package io.goodmidnight.scanner.buildsystem.extension.type

data class BuildConfigField(
    val type: Type,
    val name: String,
    val value: Any
) {
    enum class Type(val field: String) {
        StringType(field = "String"),
        IntType(field = "int"),
        BooleanType(field = "boolean")
    }
}
