package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

open class XmlTextElement internal constructor(private val text: String) : XmlElement, XmlIgnorable {

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        if (isTextEmpty()) return
        builder.append(indent)
        appendText(builder, outputOptions)
        builder.append(outputOptions.lineEnding)
    }

    open fun appendText(builder: Appendable, outputOptions: OutputOptions) {
        outputOptions.appendEscapedValue(builder, text)
    }

    private fun isTextEmpty(): Boolean {
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