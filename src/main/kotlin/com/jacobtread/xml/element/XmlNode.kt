package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Object representing a named node in the xml tree
 *
 * @property nodeName The name of this node
 * @constructor Creates a new XmlNode with the provided name
 */
open class XmlNode internal constructor(val nodeName: String) : XmlElement {

    /**
     * Map of the attributes for this node. Attribute values are allowed
     * to be any type as they are encoded to string later.
     */
    val attributes = LinkedHashMap<String, Any?>()

    /**
     * List of all elements that are children to this node. These
     * elements will be rendered inside this node.
     */
    val children = ArrayList<XmlElement>()

    // region Children Functions

    /**
     * Creates a new text element from the provided text
     * value and adds it to the children list. The text
     * value will be placed directly inside the node
     *
     * @param text The text value
     */
    fun text(text: String) {
        children.add(XmlTextElement(text))
    }

    /**
     * Creates a new comment element from the provided
     * text value and adds it to the children list
     *
     * The rendered result of this element will resemble the following:
     *
     * <!-- The provided text -->
     *
     * @param text The comment text value
     */
    fun comment(text: String) {
        children.add(XmlCommentElement(text))
    }

    /**
     * Creates a new CDATA element from the provided text
     * value and adds it to the children list
     *
     * The rendered result of this element will resemble the following:
     *
     * <![CDATA[The provided text]]>
     *
     * @param text The provided CDATA text value
     */
    fun cdata(text: String) {
        children.add(XmlCDATAElement(text))
    }

    /**
     * Creates a new Processing Instruction element from the provided
     * text and attributes then adds it to the children list
     *
     * @param text The provided processing instruction text
     * @param attributes The attributes for the
     */
    fun processingInstruction(text: String, vararg attributes: Pair<String, String>) {
        children.add(XmlPIElement(text, linkedMapOf(*attributes)))
    }

    fun processingInstruction(text: String, attributes: Map<String, String>) {
        children.add(XmlPIElement(text, LinkedHashMap(attributes)))
    }

    fun node(name: String): XmlNode {
        val xmlNode = XmlNode(name)
        children.add(xmlNode)
        return xmlNode
    }

    fun node(name: String, value: String) {
        val xmlNode = XmlNode(name)
        children.add(xmlNode)
        xmlNode.text(value)
    }

    fun node(name: String, value: Any?) {
        val xmlNode = XmlNode(name)
        children.add(xmlNode)
        if (value != null) {
            xmlNode.text(value.toString())
        }
    }

    inline fun node(name: String, init: XmlNode.() -> Unit) {
        val xmlNode = node(name)
        xmlNode.init()
    }

    // endregion

    // region Attributes
    var xmlns: String?
        get() = attribute("xmlns") as String?
        set(value) {
            attribute("xmlns", value)
        }

    fun attributes(vararg values: Pair<String, Any?>) {
        attributes.putAll(values)
    }

    fun attributes(values: Map<String, Any?>) {
        attributes.putAll(values)
    }

    fun attribute(name: String, value: Any?) {
        if (value == null) {
            attributes.remove(name)
        } else {
            attributes[name] = value
        }
    }

    fun attribute(name: String): Any? = attributes[name]
    fun hasAttribute(name: String): Boolean = attributes.containsKey(name)


    fun namespace(name: String, value: String) {
        attribute("xmlns:$name", value)
    }

    // endregion

    // region Operator Overloads

    operator fun get(attributeName: String): Any? = attribute(attributeName)
    operator fun set(attributeName: String, attributeValue: Any?) {
        attribute(attributeName, attributeValue)
    }

    operator fun String.invoke(value: String) = node(this, value)
    operator fun String.invoke(value: Any?) = node(this, value)

    inline operator fun String.invoke(init: XmlNode.() -> Unit) {
        val xmlNode = node(this)
        xmlNode.init()
    }

    inline operator fun String.invoke(attributes: Map<String, Any?>, init: XmlNode.() -> Unit) {
        val xmlNode = node(this)
        xmlNode.attributes(attributes)
        xmlNode.init()
    }

    inline operator fun String.invoke(vararg attributes: Pair<String, Any?>, init: XmlNode.() -> Unit) {
        val xmlNode = node(this)
        xmlNode.attributes(*attributes)
        xmlNode.init()
    }

    operator fun String.invoke(attributes: Map<String, Any?>) {
        val xmlNode = node(this)
        xmlNode.attributes(attributes)
    }

    operator fun String.invoke(vararg attributes: Pair<String, Any?>) {
        val xmlNode = node(this)
        xmlNode.attributes(*attributes)
    }

    operator fun String.unaryMinus() = comment(this)
    operator fun String.unaryPlus() = text(this)

    // endregion

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        // Append start of tag
        builder.append(indent)
            .append('<')
            .append(nodeName)
        // Append tag attributes
        if (attributes.isNotEmpty()) {
            builder.append(' ')
            val entries = attributes.iterator()
            if (entries.hasNext()) {
                while (true) {
                    val (key, value) = entries.next()
                    builder.append(key)
                        .append("=\"")
                    outputOptions.appendEscapedValue(builder, value)
                    builder.append('"')
                    if (!entries.hasNext()) {
                        break
                    }
                    builder.append(' ')
                }
            }
        }
        if (isContentEmpty()) { // Children tree is empty / contents are empty
            // Append closing tag / self-closing tag
            if (outputOptions.useSelfClosingTags) {
                builder.append("/>")
            } else {
                builder.append('>')
                    .append("</")
                    .append(nodeName)
                    .append('>')
            }
            builder.append(outputOptions.lineEnding)
        } else {

            if (
                outputOptions.prettyPrint
                && outputOptions.singleLineTextElements
                && isOnlyIgnorable()
            ) { // Single line text element formatting
                val textElement = children[0] as XmlTextElement
                builder.append('>')
                textElement.appendText(builder, outputOptions)
                builder.append("</")
                    .append(nodeName)
                    .append('>')
                    .append(outputOptions.lineEnding)
            } else { // Multiline children formatting
                builder.append('>')
                    .append(outputOptions.lineEnding)
                // Create the next indentation string
                val nextIndex = if (outputOptions.prettyPrint) {
                    indent + outputOptions.indent
                } else {
                    ""
                }
                children.forEach {
                    it.render(builder, nextIndex, outputOptions)
                }
                builder.append(indent)
                    .append("</")
                    .append(nodeName)
                    .append('>')
                    .append(outputOptions.lineEnding)
            }
        }
    }

    private fun isOnlyIgnorable(): Boolean {
        return children.size == 1 && children[0] is XmlIgnorable
    }

    private fun isContentEmpty(): Boolean {
        if (children.isEmpty()) return true
        if (!isOnlyIgnorable()) return false

        val textElement = children[0] as XmlIgnorable
        return textElement.isIgnorable()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is XmlNode) return false

        if (nodeName != other.nodeName) return false
        if (attributes != other.attributes) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodeName.hashCode()
        result = 31 * result + attributes.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }


}