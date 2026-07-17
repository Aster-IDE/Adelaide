package dev.adelaide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dev.adelaide.editor.EditorState
import dev.adelaide.editor.SearchController
import dev.adelaide.editor.SelectionState
import dev.adelaide.file.FileManager
import dev.adelaide.file.FileOpener
import dev.adelaide.file.RecentFiles
import dev.adelaide.ui.screens.EditorScreen
import dev.adelaide.ui.theme.AdelaideTheme
import dev.adelaide.util.lineAndColumn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val fileOpener = FileOpener()
    private val fileManager = FileManager()
    private val recentFiles = RecentFiles()
    private val searchController = SearchController()

    private var currentFileUri: Uri? = null
    private var state by mutableStateOf(EditorState())

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri == null) return@registerForActivityResult
        currentFileUri = uri
        lifecycleScope.launch {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
            val content = fileManager.loadFile(contentResolver, uri)
            val displayName = fileOpener.getDisplayName(contentResolver, uri)
            recentFiles.add(uri, displayName)
            state = state.copy(
                text = content,
                fileUri = uri,
                fileName = displayName,
                isModified = false,
                lastSavedText = content,
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdelaideTheme {
                EditorScreen(
                    state = state,
                    onOpenFile = { fileOpener.launchPicker(filePickerLauncher) },
                    onTextChanged = { text ->
                        state = state.copy(text = text, isModified = text != state.lastSavedText)
                    },
                    onSelectionChanged = { start, end ->
                        state = state.copy(selectionState = SelectionState(start, end))
                    },
                    onSave = {
                        val uri = state.fileUri
                        if (uri != null) {
                            lifecycleScope.launch {
                                val success = fileManager.saveFile(contentResolver, uri, state.text)
                                if (success) {
                                    state = state.copy(isModified = false, lastSavedText = state.text)
                                }
                            }
                        }
                    },
                    onToggleSearch = {
                        state = state.copy(isSearchVisible = !state.isSearchVisible)
                    },
                    onSearchQueryChange = { query ->
                        val updated = state.searchState.copy(query = query)
                        val matchCount = searchController.countMatches(state.text, query, state.searchState.caseSensitive)
                        state = state.copy(searchState = updated.copy(matchCount = matchCount, currentMatch = 0))
                    },
                    onSearchNext = {
                        val matchIndex = searchController.findNext(
                            state.text,
                            state.searchState.query,
                            state.searchState.caseSensitive,
                            state.selectionState.end,
                        )
                        if (matchIndex >= 0) {
                            state = state.copy(selectionState = SelectionState(matchIndex, matchIndex + state.searchState.query.length))
                        }
                    },
                    onSearchPrevious = {
                        val matchIndex = searchController.findPrevious(
                            state.text,
                            state.searchState.query,
                            state.searchState.caseSensitive,
                            state.selectionState.start,
                        )
                        if (matchIndex >= 0) {
                            state = state.copy(selectionState = SelectionState(matchIndex, matchIndex + state.searchState.query.length))
                        }
                    },
                    onReplaceQueryChange = { query ->
                        state = state.copy(searchState = state.searchState.copy(replaceQuery = query))
                    },
                    onCaseSensitiveChange = { enabled ->
                        state = state.copy(searchState = state.searchState.copy(caseSensitive = enabled))
                    },
                    onReplace = {
                        val replacement = state.searchState.replaceQuery
                        val updatedText = searchController.replace(
                            state.text,
                            state.searchState.query,
                            replacement,
                            state.searchState.caseSensitive,
                            state.selectionState.end,
                        )
                        state = state.copy(text = updatedText, isModified = true)
                    },
                    onReplaceAll = {
                        val replacement = state.searchState.replaceQuery
                        val updatedText = searchController.replaceAll(
                            state.text,
                            state.searchState.query,
                            replacement,
                            state.searchState.caseSensitive,
                        )
                        state = state.copy(text = updatedText, isModified = true)
                    },
                )
            }
        }
    }
}
