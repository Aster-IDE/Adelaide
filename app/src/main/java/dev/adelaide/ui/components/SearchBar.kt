package dev.adelaide.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String,
    replaceQuery: String,
    caseSensitive: Boolean,
    onQueryChange: (String) -> Unit,
    onReplaceChange: (String) -> Unit,
    onCaseSensitiveChange: (Boolean) -> Unit,
    onFindNext: () -> Unit,
    onFindPrevious: () -> Unit,
    onReplace: () -> Unit,
    onReplaceAll: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Find") },
        )
        OutlinedTextField(
            value = replaceQuery,
            onValueChange = onReplaceChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            singleLine = true,
            label = { Text("Replace") },
        )
        androidx.compose.foundation.layout.Row(modifier = Modifier.padding(top = 8.dp)) {
            Checkbox(checked = caseSensitive, onCheckedChange = onCaseSensitiveChange)
            Text(text = "Case sensitive", modifier = Modifier.padding(start = 8.dp))
        }
        androidx.compose.foundation.layout.Row(modifier = Modifier.padding(top = 8.dp)) {
            androidx.compose.material3.Button(onClick = onFindNext) { Text("Next") }
            androidx.compose.material3.Button(onClick = onFindPrevious, modifier = Modifier.padding(start = 8.dp)) { Text("Previous") }
            androidx.compose.material3.Button(onClick = onReplace, modifier = Modifier.padding(start = 8.dp)) { Text("Replace") }
            androidx.compose.material3.Button(onClick = onReplaceAll, modifier = Modifier.padding(start = 8.dp)) { Text("All") }
        }
    }
}
