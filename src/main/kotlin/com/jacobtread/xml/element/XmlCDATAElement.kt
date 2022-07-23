package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Represents a CDATA element within the XML tree
 *
 * @property text The CDATA text value
 * @constructor Create a new CDATA Element
 */
class XmlCDATAElement internal constructor(val text: String) : XmlElement, XmlIgnorable {

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        builder.append(indent)
            .append("<![CDATA[")
        val cdataEnd = "]]>"
        val cdataStart = "<![CDATA["
        val escapedText = text.replace(cdataEnd, "]]$cdataEnd$cdataStart>")
        builder.append(escapedText)
        builder.append("]]>")
            .append(outputOptions.lineEnding)
    }

    override fun isIgnorable(): Boolean {
        return text.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlCDATAElement) return false
        if (text != other.text) return false
        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}