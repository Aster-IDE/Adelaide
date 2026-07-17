package dev.adelaide.file

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileManager {
    suspend fun loadFile(contentResolver: ContentResolver, uri: Uri): String = withContext(Dispatchers.IO) {
        FileOpener().readText(contentResolver, uri)
    }

    suspend fun saveFile(contentResolver: ContentResolver, uri: Uri, content: String): Boolean = withContext(Dispatchers.IO) {
        try {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(content.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (_: IOException) {
            false
        }
    }

    suspend fun reloadFile(contentResolver: ContentResolver, uri: Uri): String = loadFile(contentResolver, uri)
}
