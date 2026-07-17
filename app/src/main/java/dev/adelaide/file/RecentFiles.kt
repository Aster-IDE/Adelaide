package dev.adelaide.file

import android.net.Uri

class RecentFiles {
    private val entries = mutableListOf<RecentFileEntry>()

    fun add(uri: Uri, displayName: String) {
        entries.removeAll { it.uri == uri }
        entries.add(0, RecentFileEntry(uri, displayName))
        if (entries.size > 10) {
            entries.removeAt(entries.lastIndex)
        }
    }

    fun list(): List<RecentFileEntry> = entries.toList()

    fun clear() {
        entries.clear()
    }
}

data class RecentFileEntry(
    val uri: Uri,
    val displayName: String,
)
