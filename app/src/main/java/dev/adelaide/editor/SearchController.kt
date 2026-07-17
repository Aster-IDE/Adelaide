package dev.adelaide.editor

import kotlin.math.max
import kotlin.math.min

class SearchController {
    fun findNext(text: String, query: String, caseSensitive: Boolean, fromIndex: Int = 0): Int {
        if (query.isBlank()) return -1
        val normalizedText = normalize(text, caseSensitive)
        val normalizedQuery = normalize(query, caseSensitive)
        val start = max(0, min(fromIndex, normalizedText.length))
        val index = normalizedText.indexOf(normalizedQuery, start)
        return if (index >= 0) index else -1
    }

    fun findPrevious(text: String, query: String, caseSensitive: Boolean, fromIndex: Int = 0): Int {
        if (query.isBlank()) return -1
        val normalizedText = normalize(text, caseSensitive)
        val normalizedQuery = normalize(query, caseSensitive)
        val safeStart = max(0, min(fromIndex, normalizedText.length))
        val startSearch = if (safeStart > 0) safeStart - 1 else 0
        var index = normalizedText.lastIndexOf(normalizedQuery, startSearch)
        if (index < 0 && normalizedText.length > 0) {
            index = normalizedText.lastIndexOf(normalizedQuery)
        }
        return index
    }

    fun countMatches(text: String, query: String, caseSensitive: Boolean): Int {
        if (query.isBlank()) return 0
        val normalizedText = normalize(text, caseSensitive)
        val normalizedQuery = normalize(query, caseSensitive)
        var count = 0
        var start = 0
        while (start <= normalizedText.length) {
            val index = normalizedText.indexOf(normalizedQuery, start)
            if (index < 0) break
            count += 1
            start = index + normalizedQuery.length
        }
        return count
    }

    fun replace(text: String, query: String, replacement: String, caseSensitive: Boolean, fromIndex: Int = 0): String {
        val index = findNext(text, query, caseSensitive, fromIndex)
        if (index < 0) return text
        return text.substring(0, index) + replacement + text.substring(index + query.length)
    }

    fun replaceAll(text: String, query: String, replacement: String, caseSensitive: Boolean): String {
        if (query.isBlank()) return text
        val normalizedText = normalize(text, caseSensitive)
        val normalizedQuery = normalize(query, caseSensitive)
        val builder = StringBuilder()
        var start = 0
        while (start <= normalizedText.length) {
            val index = normalizedText.indexOf(normalizedQuery, start)
            if (index < 0) {
                builder.append(text.substring(start))
                break
            }
            builder.append(text.substring(start, index))
            builder.append(replacement)
            start = index + normalizedQuery.length
        }
        return builder.toString()
    }

    private fun normalize(value: String, caseSensitive: Boolean): String {
        return if (caseSensitive) value else value.lowercase()
    }
}
