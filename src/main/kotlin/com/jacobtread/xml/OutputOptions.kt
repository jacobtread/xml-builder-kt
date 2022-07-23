package com.jacobtread.xml

data class OutputOptions(
    val prettyPrint: Boolean = true,
    val singleLineTextElements: Boolean = true,
    val useSelfClosingTags: Boolean = true,
    val useCharacterReferences: Boolean = false,
    val indent: String = "\t",
) {
    internal var xmlVersion: XmlVersion = XmlVersion.V10
    internal val lineEnding = if (prettyPrint) System.lineSeparator() else ""


    internal fun appendEscapedValue(
        builder: Appendable,
        value: Any?,
    ) {
        val stringValue = value?.toString()
        if (stringValue == null) {
            builder.append(null)
        } else {
            appendEscapedText(builder, stringValue)
        }
    }

    internal fun appendEscapedText(builder: Appendable, value: String) {
        if (useCharacterReferences) {
            appendReferenceCharacter(builder, value)
        } else {
            xmlVersion.appendEscapedValue(builder, value)
        }
    }

    private fun appendReferenceCharacter(builder: Appendable, value: String) {
        value.forEach { char ->
            when (char) {
                '\'' -> builder.append("&#39;")
                '&' -> builder.append("&#38;")
                '<' -> builder.append("&#60;")
                '>' -> builder.append("&#62;")
                '"' -> builder.append("&#34;")
                else -> builder.append(char)
            }
        }
    }
}
