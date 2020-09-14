package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    if (substr.trim().isEmpty()) return emptyList()
    val result = mutableListOf<Int>()
    var index = this?.indexOf(string = substr, ignoreCase = ignoreCase) ?: 0
    while (index >= 0) {
        result.add(index)
        index = this?.indexOf(substr, index + 1, ignoreCase) ?: -1
    }
    return result
}