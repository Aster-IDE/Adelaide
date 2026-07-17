package dev.adelaide.syntax

enum class Language {
    PLAIN_TEXT,
    C,
    CPP,
    KOTLIN,
    RUST,
}

enum class TokenType {
    KEYWORD,
    STRING,
    COMMENT,
    NUMBER,
    PLAIN,
}

data class Token(
    val type: TokenType,
    val start: Int,
    val end: Int,
)

class Tokenizer(private val language: Language) {
    private val keywords = when (language) {
        Language.PLAIN_TEXT -> emptySet<String>()
        Language.C -> setOf(
            "auto", "break", "case", "char", "const", "continue", "default", "do", "double",
            "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register",
            "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef",
            "union", "unsigned", "void", "volatile", "while",
        )
        Language.CPP -> setOf(
            "alignas", "alignof", "and", "and_eq", "asm", "auto", "bitand", "bitor", "bool",
            "break", "case", "catch", "char", "class", "compl", "const", "const_cast",
            "continue", "decltype", "default", "delete", "do", "double", "dynamic_cast", "else",
            "enum", "explicit", "export", "extern", "false", "float", "for", "friend", "goto",
            "if", "inline", "int", "long", "mutable", "namespace", "new", "noexcept", "not",
            "not_eq", "nullptr", "operator", "or", "or_eq", "private", "protected", "public",
            "register", "reinterpret_cast", "return", "short", "signed", "sizeof", "static",
            "static_cast", "struct", "switch", "template", "this", "thread_local", "throw", "true",
            "try", "typedef", "typeid", "typename", "union", "unsigned", "using", "virtual",
            "void", "volatile", "wchar_t", "while", "xor", "xor_eq",
        )
        Language.KOTLIN -> setOf(
            "abstract", "as", "break", "by", "catch", "class", "companion", "const", "continue",
            "data", "do", "else", "enum", "false", "final", "for", "fun", "if", "import", "in",
            "interface", "internal", "is", "null", "object", "open", "operator", "out",
            "override", "package", "private", "protected", "public", "return", "sealed", "super",
            "this", "throw", "to", "true", "try", "typealias", "val", "var", "when", "while",
        )
        Language.RUST -> setOf(
            "as", "break", "const", "continue", "crate", "else", "enum", "extern", "false", "fn",
            "for", "if", "impl", "in", "let", "loop", "match", "mod", "move", "mut", "pub", "ref",
            "return", "self", "Self", "static", "struct", "super", "trait", "true", "type",
            "unsafe", "use", "where", "while",
        )
    }

    fun tokenize(text: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var index = 0
        while (index < text.length) {
            val current = text[index]
            if (current == '/' && index + 1 < text.length && text[index + 1] == '/') {
                val start = index
                while (index < text.length && text[index] != '\n') {
                    index += 1
                }
                tokens.add(Token(TokenType.COMMENT, start, index))
                continue
            }
            if (current == '/' && index + 1 < text.length && text[index + 1] == '*') {
                val start = index
                index += 2
                while (index + 1 < text.length && !(text[index] == '*' && text[index + 1] == '/')) {
                    index += 1
                }
                if (index + 1 < text.length) {
                    index += 2
                }
                tokens.add(Token(TokenType.COMMENT, start, index))
                continue
            }
            if (current == '' || current == '\'') {
                val quote = current
                val start = index
                index += 1
                while (index < text.length) {
                    if (text[index] == '\\' && index + 1 < text.length) {
                        index += 2
                        continue
                    }
                    if (text[index] == quote) break
                    index += 1
                }
                if (index < text.length) index += 1
                tokens.add(Token(TokenType.STRING, start, index))
                continue
            }
            if (current.isDigit()) {
                val start = index
                index += 1
                while (index < text.length && (text[index].isDigit() || text[index] == '.')) {
                    index += 1
                }
                tokens.add(Token(TokenType.NUMBER, start, index))
                continue
            }
            if (current.isLetter() || current == '_') {
                val start = index
                index += 1
                while (index < text.length && (text[index].isLetterOrDigit() || text[index] == '_')) {
                    index += 1
                }
                val word = text.substring(start, index)
                val tokenType = if (keywords.contains(word)) TokenType.KEYWORD else TokenType.PLAIN
                tokens.add(Token(tokenType, start, index))
                continue
            }
            index += 1
        }
        return tokens
    }
}
