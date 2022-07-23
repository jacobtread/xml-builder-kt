package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

class XmlPIElement internal constructor(val text: String, val attributes: Map<String, String>) : XmlElement, XmlIgnorable {
    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        builder.append(indent)
            .append("<?")
            .append(text)
        if (attributes.isNotEmpty()) {
            val entries = attributes.iterator()
            if (entries.hasNext()) {
                while (true) {
                    val (key, value) = entries.next()
                    builder.append(key)
                        .append("=\"")
                        .append(value)
                        .append('"')
                    if (!entries.hasNext()) {
                        break
                    }
                    builder.append(' ')
                }
            }
        }
        builder.append(outputOptions.lineEnding)
    }


    override fun isIgnorable(): Boolean {
        return text.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlPIElement) return false

        if (text != other.text) return false
        if (attributes != other.attributes) return false
        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + attributes.hashCode()
        return result
    }
}