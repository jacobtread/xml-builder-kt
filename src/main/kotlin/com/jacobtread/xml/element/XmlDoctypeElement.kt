package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Element representing the DOCTYPE for the root XML element
 *
 * @property name The DOCTYPE name
 * @property systemId The system id
 * @property publicId The public id
 * @constructor Creates a new DOCTYPE element
 */
class XmlDoctypeElement(
    private val name: String,
    private val systemId: String? = null,
    private val publicId: String? = null,
) : XmlElement {

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        builder.append("<!DOCTYPE ")
            .append(name)
        val isPublicSet = publicId != null
        val isSystemSet = systemId != null
        if(isPublicSet) {
            builder.append(" PUBLIC \"")
                .append(publicId)
                .append('"')
        }
        if (isSystemSet) {
            if(!isPublicSet) builder.append(" SYSTEM")
            builder.append(" \"")
                .append(systemId)
                .append('"')
        }
        builder.appendLine(">")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlDoctypeElement) return false

        if (name != other.name) return false
        if (systemId != other.systemId) return false
        if (publicId != other.publicId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (systemId?.hashCode() ?: 0)
        result = 31 * result + (publicId?.hashCode() ?: 0)
        return result
    }
}