package dev.adelaide.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdelaideTopBar(
    title: String,
    onOpenFile: () -> Unit,
    onSave: () -> Unit,
    onToggleSearch: () -> Unit,
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(onClick = onOpenFile) { Icon(Icons.Filled.FolderOpen, contentDescription = null) }
            IconButton(onClick = onSave) { Icon(Icons.Filled.Save, contentDescription = null) }
            IconButton(onClick = onToggleSearch) { Icon(Icons.Filled.Search, contentDescription = null) }
        },
    )
}
