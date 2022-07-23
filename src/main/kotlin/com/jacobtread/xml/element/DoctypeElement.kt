package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

class DoctypeElement(
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
}