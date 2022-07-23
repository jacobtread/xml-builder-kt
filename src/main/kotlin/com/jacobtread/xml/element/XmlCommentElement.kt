package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

class XmlCommentElement internal constructor(val text: String) : XmlElement, XmlIgnorable {
    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        val escapedText = text.replace("--", "&#45;&#45;\"")
        builder.append(indent)
            .append("<!-- ")
            .append(escapedText)
            .append(" -->")
            .append(outputOptions.lineEnding)
    }

    override fun isIgnorable(): Boolean {
        return text.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlCommentElement) return false
        if (text != other.text) return false
        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}