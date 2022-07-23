package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

open class XmlNode(val nodeName: String) : XmlElement {

    val attributes = LinkedHashMap<String, Any?>()
    val children = ArrayList<XmlElement>()


    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        val lineEnding = if (outputOptions.prettyPrint) System.lineSeparator() else ""
        // Append start of tag
        builder.append(indent)
            .append('<')
            .append(nodeName)
            .append(' ')
        // Append tag attributes
        if (attributes.isNotEmpty()) {
            val entries = attributes.iterator()
            if (entries.hasNext()) {
                while (true) {
                    val (key, value) = entries.next()
                    builder.append(key)
                        .append("=\"")
                    appendEscapedValue(builder, value, outputOptions)
                    builder.append('"')
                    if (!entries.hasNext()) {
                        break
                    }
                    builder.append(' ')
                }
            }
        }
        if (isEmpty()) { // Children tree is empty / contents are empty
            // Append closing tag / self-closing tag
            if (outputOptions.useSelfClosingTags) {
                builder.append("/>")
                    .append(lineEnding)
            } else {
                builder.append('>')
                appendClosing(builder, lineEnding)
            }
        } else {

            if (
                outputOptions.prettyPrint
                && outputOptions.singleLineTextElements
                && isOnlyTextElement()
            ) { // Single line text element formatting
                val textElement = children[0] as XmlTextElement
                builder.append('>')
                appendEscapedValue(builder, textElement.text, outputOptions)
                appendClosing(builder, lineEnding)
            } else { // Multiline children formatting
                builder.append('>')
                    .append(lineEnding)
                // Create the next indentation string
                val nextIndex = nextIndent(outputOptions, indent)
                children.forEach {
                    it.render(builder, nextIndex, outputOptions)
                }
                builder.append(indent)
                appendClosing(builder, lineEnding)
            }
        }
    }

    private fun nextIndent(outputOptions: OutputOptions, indent: String): String {
        return if (outputOptions.prettyPrint) {
            indent + outputOptions.indent
        } else {
            ""
        }
    }

    private fun appendClosing(builder: Appendable, lineEnding: String) {
        builder.append("</")
            .append(nodeName)
            .append('>')
            .append(lineEnding)
    }

    private fun appendEscapedValue(
        builder: Appendable,
        value: Any?,
        outputOptions: OutputOptions,
    ) {
        val stringValue = value?.toString()
        if (stringValue == null) {
            builder.append(null)
        } else if (outputOptions.useCharacterReferences) {
            appendReferenceCharacter(builder, stringValue)
        } else {
            outputOptions.xmlVersion.appendEscapedValue(builder, stringValue)
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


    private fun isOnlyTextElement(): Boolean {
        return children.size == 1 && children[0] is XmlTextElement
    }

    override fun isEmpty(): Boolean {
        return children.all { it.isEmpty() }
    }
}