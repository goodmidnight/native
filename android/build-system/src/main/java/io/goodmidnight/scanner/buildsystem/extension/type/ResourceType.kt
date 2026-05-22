package io.goodmidnight.scanner.buildsystem.extension.type

data class ResourceType(
    val type: Type,
    val name: String,
    val value: Any
) {
    enum class Type(val field: String) {
        StringType(field = "string"),
        IntType(field = "int"),
        BooleanType(field = "boolean")
    }
}
