package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Represents a piece of text within the XML tree. This element is intended to be
 * created via the builder function inside Node [XmlNode.text]
 *
 * @property text The text value of this node
 * @constructor Create empty Xml text element
 */
open class XmlTextElement internal constructor(val text: String) : XmlElement, XmlIgnorable {

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        if (isTextBlank()) return
        builder.append(indent)
        outputOptions.appendEscapedValue(builder, text)
        builder.append(outputOptions.lineEnding)
    }

    /**
     * Returns whether the text is blank or not if
     * all new lines and carriage returns are removed
     *
     * @return Whether the text is blank
     */
    private fun isTextBlank(): Boolean {
        return text.trim('\n', '\r').isBlank()
    }

    override fun isIgnorable(): Boolean {
        return text.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlTextElement) return false
        if (text != other.text) return false
        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}