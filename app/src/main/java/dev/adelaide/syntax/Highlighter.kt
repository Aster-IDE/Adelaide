package dev.adelaide.syntax

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.adelaide.syntax.themes.DarkSyntaxTheme
import dev.adelaide.syntax.themes.SyntaxTheme

class Highlighter(
    private val language: Language = Language.PLAIN_TEXT,
    private val theme: SyntaxTheme = DarkSyntaxTheme(),
) {
    fun highlight(text: String): AnnotatedString {
        val tokenizer = Tokenizer(language)
        val tokens = tokenizer.tokenize(text)
        return buildAnnotatedString {
            var cursor = 0
            tokens.forEach { token ->
                if (token.start > cursor) {
                    append(text.substring(cursor, token.start))
                }
                val style = when (token.type) {
                    TokenType.KEYWORD -> SpanStyle(color = theme.keyword)
                    TokenType.STRING -> SpanStyle(color = theme.string)
                    TokenType.COMMENT -> SpanStyle(color = theme.comment)
                    TokenType.NUMBER -> SpanStyle(color = theme.number)
                    TokenType.PLAIN -> SpanStyle(color = theme.plain)
                }
                withStyle(style) {
                    append(text.substring(token.start, token.end))
                }
                cursor = token.end
            }
            if (cursor < text.length) {
                append(text.substring(cursor))
            }
        }
    }
}
