package dev.adelaide.editor

import android.net.Uri

data class EditorState(
    val text: String = "",
    val fileUri: Uri? = null,
    val fileName: String = "Untitled",
    val isModified: Boolean = false,
    val lastSavedText: String = "",
    val searchState: SearchState = SearchState(),
    val selectionState: SelectionState = SelectionState(),
    val isSearchVisible: Boolean = false,
    val isReplaceVisible: Boolean = false,
)

data class SearchState(
    val query: String = "",
    val replaceQuery: String = "",
    val caseSensitive: Boolean = false,
    val matchCount: Int = 0,
    val currentMatch: Int = 0,
)

data class SelectionState(
    val start: Int = 0,
    val end: Int = 0,
)
