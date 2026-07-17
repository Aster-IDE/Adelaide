package dev.adelaide.util

fun String.lineAndColumn(index: Int): Pair<Int, Int> {
    val safeIndex = index.coerceIn(0, length)
    val line = substring(0, safeIndex).count { it == '\n' } + 1
    val lineStart = lastIndexOf('\n', safeIndex - 1) + 1
    val column = safeIndex - lineStart + 1
    return line to column
}

fun String.safeSubstring(start: Int, end: Int): String {
    val safeStart = start.coerceIn(0, length)
    val safeEnd = end.coerceIn(safeStart, length)
    return substring(safeStart, safeEnd)
}
