package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions
import com.jacobtread.xml.XmlVersion

/**
 * Represents the root node which is the outermost node in a xml
 * node tree. This contains the root state of the xml document
 *
 * @constructor Creates a new xml root node
 *
 * @param nodeName The name of this xml node
 */
class XmlRootNode(nodeName: String) : XmlNode(nodeName) {

    /**
     * Determines whether to include the XML Prolog which looks like
     * ```
     * <?xml version="1.0" encoding="UTF-8"?>
     * ```
     */
    var includeXmlProlog = false

    /**
     * The DOCTYPE for this root node. This is null by default, so it
     * is not rendered unless one is specified
     */
    var docType: XmlDoctypeElement? = null

    /**
     * Determines which encoding is listed in the XML Prolog this doesn't affect
     * the actual string encoding. Setting this value enables the XML Prolog
     */
    var encoding: String = "UTF-8"
        set(value) {
            includeXmlProlog = true
            field = value
        }

    /**
     * Determines which xml version is listed in the XML Prolog and affects xml escaping.
     * Setting this value enables the XML Prolog
     */
    var version: XmlVersion = XmlVersion.V10
        set(value) {
            includeXmlProlog = true
            field = value
        }

    /**
     * Determines the value of the standalone attribute in the xml prolog. This value is
     * omitted unless a value is provided. Providing a value enables XML Prolog
     */
    var standalone: Boolean? = null
        set(value) {
            includeXmlProlog = true
            field = value
        }

    /**
     * List of processing instructions which are on the outer level outside
     * the root node element. These are rendered before the root node. This
     * list is null until an instruction is added
     */
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

    /**
     * The default java toString function. This is
     * redirected to the custom toString function
     * and provides the default output options
     *
     * @return The rendered XML tree
     */
    override fun toString(): String {
        return appendToBuilder().toString()
    }

    /**
     * toString function which takes in custom output
     * options and returns the rendered XML tree
     *
     * @param outputOptions The output options
     * @return The rendered XML tree
     */
    fun toString(outputOptions: OutputOptions): String {
        return appendToBuilder(outputOptions = outputOptions).toString()
    }

    /**
     * Appends the rendered XML tree to the provided appendable
     * object and then returns that appendable object
     *
     * @param builder The builder to append to (defaults to a new string builder)
     * @param outputOptions The output options
     * @return The builder that was appended to
     */
    fun appendToBuilder(
        builder: Appendable = StringBuilder(),
        outputOptions: OutputOptions = OutputOptions(),
    ): Appendable {
        render(builder, "", outputOptions)
        return builder
    }

    /**
     * Sets the DOCTYPE element for this xml root node. This is appended
     * to the top of the output
     *
     * @param name The doctype name
     * @param publicId The doctype public id
     * @param systemId The doctype system id
     */
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


    /**
     * Creates a new Global Processing Instruction element from the provided
     * text and attributes these are rendered before the root xml node
     *
     * @param text The provided processing instruction text
     * @param attributes The attributes for the instruction
     */
    fun globalProcessingInstruction(text: String, vararg attributes: Pair<String, String>) {
        if (globalProcessingInstructions == null) {
            globalProcessingInstructions = ArrayList()
        }
        globalProcessingInstructions!!.add(XmlPIElement(text, linkedMapOf(*attributes)))
    }

    /**
     * Creates a new Global Processing Instruction element from the provided
     * text and attributes these are rendered before the root xml node
     *
     * @param text The provided processing instruction text
     * @param attributes The attributes for the instruction
     */
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