package dev.adelaide.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import dev.adelaide.syntax.Highlighter
import dev.adelaide.syntax.Language

@Composable
fun EditorView(
    state: EditorState,
    onTextChanged: (String) -> Unit,
    onSelectionChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val language = remember(state.fileName) { detectLanguage(state.fileName) }
    val highlightedText = remember(state.text, language) {
        Highlighter(language).highlight(state.text)
    }
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = highlightedText,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(scrollState),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp,
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BasicTextField(
            value = TextFieldValue(
                text = state.text,
                selection = TextRange(state.selectionState.start, state.selectionState.end),
            ),
            onValueChange = { value ->
                onTextChanged(value.text)
                onSelectionChanged(value.selection.start, value.selection.end)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(scrollState),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp,
                color = Color.Transparent,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxSize()) {
                    innerTextField()
                }
            },
        )
    }
}

private fun detectLanguage(fileName: String): Language {
    val name = fileName.lowercase()
    return when {
        name.endsWith(".c") || name.endsWith(".h") -> Language.C
        name.endsWith(".cpp") || name.endsWith(".cc") || name.endsWith(".cxx") -> Language.CPP
        name.endsWith(".kt") || name.endsWith(".kts") -> Language.KOTLIN
        name.endsWith(".rs") -> Language.RUST
        else -> Language.PLAIN_TEXT
    }
}
