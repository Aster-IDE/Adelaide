package dev.adelaide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.adelaide.editor.EditorState
import dev.adelaide.editor.EditorView
import dev.adelaide.ui.components.AdelaideTopBar
import dev.adelaide.ui.components.EditorToolbar
import dev.adelaide.ui.components.SearchBar
import dev.adelaide.ui.components.StatusBar

@Composable
fun EditorScreen(
    state: EditorState,
    onOpenFile: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSelectionChanged: (Int, Int) -> Unit,
    onSave: () -> Unit,
    onToggleSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchNext: () -> Unit,
    onSearchPrevious: () -> Unit,
    onReplaceQueryChange: (String) -> Unit,
    onCaseSensitiveChange: (Boolean) -> Unit,
    onReplace: () -> Unit,
    onReplaceAll: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AdelaideTopBar(
            title = state.fileName,
            onOpenFile = onOpenFile,
            onSave = onSave,
            onToggleSearch = onToggleSearch,
        )
        EditorToolbar(fileName = state.fileName, isModified = state.isModified)
        if (state.isSearchVisible) {
            SearchBar(
                query = state.searchState.query,
                replaceQuery = state.searchState.replaceQuery,
                caseSensitive = state.searchState.caseSensitive,
                onQueryChange = onSearchQueryChange,
                onReplaceChange = onReplaceQueryChange,
                onCaseSensitiveChange = onCaseSensitiveChange,
                onFindNext = onSearchNext,
                onFindPrevious = onSearchPrevious,
                onReplace = onReplace,
                onReplaceAll = onReplaceAll,
            )
        }
        EditorView(
            state = state,
            onTextChanged = onTextChanged,
            onSelectionChanged = onSelectionChanged,
            modifier = Modifier.weight(1f),
        )
        val (line, column) = state.text.lineAndColumn(state.selectionState.end)
        StatusBar(
            line = line,
            column = column,
            matchCount = state.searchState.matchCount,
        )
    }
}
