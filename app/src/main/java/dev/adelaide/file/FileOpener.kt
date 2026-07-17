package dev.adelaide.file

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

class FileOpener {
    fun launchPicker(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(
            arrayOf(
                "text/plain",
                "text/markdown",
                "text/x-java-source",
                "text/x-kotlin",
                "application/octet-stream",
            ),
        )
    }

    fun readText(contentResolver: ContentResolver, uri: Uri): String {
        return contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8).use { reader ->
            reader?.readText().orEmpty()
        }
    }

    fun getDisplayName(contentResolver: ContentResolver, uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use { c ->
            val nameIndex = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && c.moveToFirst()) {
                c.getString(nameIndex)
            } else {
                uri.lastPathSegment ?: "file"
            }
        } ?: uri.lastPathSegment ?: "file"
    }
}
