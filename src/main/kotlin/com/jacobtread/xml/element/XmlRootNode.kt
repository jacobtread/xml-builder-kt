package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions
import com.jacobtread.xml.XmlVersion

class XmlRootNode(nodeName: String) : XmlNode(nodeName) {

    private var includeXmlProlog = false

    var docType: XmlDoctypeElement? = null

    var encoding: String = "UTF-8"
        set(value) {
            includeXmlProlog = true
            field = value
        }

    var version: XmlVersion = XmlVersion.V10
        set(value) {
            includeXmlProlog = true
            field = value
        }

    var standalone: Boolean? = null
        set(value) {
            includeXmlProlog = true
            field = value
        }

    private var globalProcessingInstructions: ArrayList<XmlPIElement>? = null

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        outputOptions.xmlVersion = version
        // Extra beforehand rendering
        if (includeXmlProlog) {
            builder.append("<?xml version=\"")
                .append(version.versionNumber)
                .append("\" encoding=\"")
                .append(encoding)
                .append('"')

            val standalone = standalone
            if (standalone != null) {
                builder.append(" standalone=\"")
                if (standalone) {
                    builder.append("yes")
                } else {
                    builder.append("no")
                }
                builder.append('"')
            }

            builder.append("?>")
                .append(outputOptions.lineEnding)
        }

        docType?.render(builder, "", outputOptions)

        globalProcessingInstructions?.forEach { it.render(builder, "", outputOptions) }

        // Render underlying node
        super.render(builder, indent, outputOptions)
    }

    fun doctype(
        name: String? = null,
        publicId: String? = null,
        systemId: String? = null,
    ) {
        if (publicId != null && systemId == null) {
            throw IllegalStateException("systemId must be provided if publicId is provided")
        }
        docType = XmlDoctypeElement(name ?: nodeName, publicId, systemId)
    }

    fun globalProcessingInstruction(text: String, vararg attributes: Pair<String, String>) {
        if (globalProcessingInstructions == null) {
            globalProcessingInstructions = ArrayList()
        }
        globalProcessingInstructions!!.add(XmlPIElement(text, linkedMapOf(*attributes)))
    }

    fun globalProcessingInstruction(text: String, attributes: Map<String, String>) {
        if (globalProcessingInstructions == null) {
            globalProcessingInstructions = ArrayList()
        }
        globalProcessingInstructions!!.add(XmlPIElement(text, LinkedHashMap(attributes)))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlRootNode) return false
        if (!super.equals(other)) return false

        if (includeXmlProlog != other.includeXmlProlog) return false
        if (docType != other.docType) return false
        if (encoding != other.encoding) return false
        if (version != other.version) return false
        if (standalone != other.standalone) return false
        if (globalProcessingInstructions != other.globalProcessingInstructions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + includeXmlProlog.hashCode()
        result = 31 * result + (docType?.hashCode() ?: 0)
        result = 31 * result + encoding.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + (standalone?.hashCode() ?: 0)
        result = 31 * result + (globalProcessingInstructions?.hashCode() ?: 0)
        return result
    }
}