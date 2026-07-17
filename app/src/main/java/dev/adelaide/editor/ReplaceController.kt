package dev.adelaide.editor

class ReplaceController(
    private val searchController: SearchController = SearchController(),
) {
    fun replace(text: String, query: String, replacement: String, caseSensitive: Boolean, fromIndex: Int = 0): String {
        return searchController.replace(text, query, replacement, caseSensitive, fromIndex)
    }

    fun replaceAll(text: String, query: String, replacement: String, caseSensitive: Boolean): String {
        return searchController.replaceAll(text, query, replacement, caseSensitive)
    }
}
