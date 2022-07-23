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
     *```
     * <!-- The provided text -->
     *```
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
     * ```
     * <![CDATA[The provided text]]>
     * ```
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
     * @param attributes The attributes for the instruction
     */
    fun processingInstruction(text: String, vararg attributes: Pair<String, String>) {
        children.add(XmlPIElement(text, linkedMapOf(*attributes)))
    }

    /**
     * Creates a new Processing Instruction element from the provided
     * text and attributes then adds it to the children list
     *
     * @param text The provided processing instruction text
     * @param attributes The attributes for the instruction
     */
    fun processingInstruction(text: String, attributes: Map<String, String>) {
        children.add(XmlPIElement(text, LinkedHashMap(attributes)))
    }

    /**
     * Creates a new XML node with the provided name
     * adds it to the list of children and return it
     *
     * @param name The name to give the node
     * @return The created node
     */
    fun node(name: String): XmlNode {
        val xmlNode = XmlNode(name)
        children.add(xmlNode)
        return xmlNode
    }

    /**
     * Creates a new XML node with the provided name
     * and adds a text value of the provided string value
     * this node is added to the children
     *
     * @param name The name to give the node
     * @param value The text value to put inside the node
     */
    fun textNode(name: String, value: String) {
        val xmlNode = node(name)
        xmlNode.text(value)
    }

    /**
     * Creates a new XML node with the provided name
     * and if the provided value is not null the toString
     * of that value is added as a text element child for
     * the created node
     *
     * NOTE: This function is not for adding nodes/elements those should
     * be added via the [addElement] function this is for values that
     * can be converted to string such as numbers
     *
     * @param name The name to give the node
     * @param value The value to put inside the node
     */
    fun textNode(name: String, value: Any?) {
        val xmlNode = node(name)
        if (value != null) {
            xmlNode.text(value.toString())
        }
    }

    /**
     * This function is a builder function for creating a
     * node and populating its tree. This creates a new node
     * with the provided name and passes it as the receiver
     * to the [init] function
     *
     * @param name The name to give the node
     * @param init The function which creates the tree
     * @receiver The created node is used as the receiver
     */
    inline fun node(name: String, init: XmlNode.() -> Unit) {
        val xmlNode = node(name)
        xmlNode.init()
    }

    /**
     * Adds the provided element to the children list.
     *
     * Note: You should use the builder methods instead
     * of calling this function unless you create a custom
     * XmlElement implementation?
     *
     * @param element The element to add
     */
    fun addElement(element: XmlElement) {
        children.add(element)
    }

    // endregion

    // region Attributes

    /**
     * Shortcut getter and setter variable for setting
     * and retrieving the xmlns attribute
     */
    var xmlns: String?
        get() = attribute("xmlns") as String?
        set(value) {
            attribute("xmlns", value)
        }

    /**
     * Function for providing an array of attributes represented
     * as a pair. The provided attributes are added to the
     * attributes map
     *
     * @param values The attribute values
     */
    fun attributes(vararg values: Pair<String, Any?>) {
        attributes.putAll(values)
    }

    /**
     * Function for providing a map of attributes. The
     * provided attributes map is added to the node
     * attributes map
     *
     * @param values The attributes map
     */
    fun attributes(values: Map<String, Any?>) {
        attributes.putAll(values)
    }

    /**
     * Function for setting an individual attribute.
     * If the value provided to this function is null
     * then the attribute of [name] will be removed
     * from the attributes map
     *
     * @param name The name of the attribute
     * @param value The value of the attribute or null to remove
     */
    fun attribute(name: String, value: Any?) {
        if (value == null) {
            attributes.remove(name)
        } else {
            attributes[name] = value
        }
    }

    /**
     * Retrieves an attribute from the attributes map
     * and returns the result (possibly null)
     *
     * @param name The name of the attribute
     * @return The value found with that name or null
     */
    fun attribute(name: String): Any? = attributes[name]

    /**
     * Checks the attributes map to see if there
     * is an attribute with the provided name
     *
     * @param name The name of the attribute
     * @return Whether the attribute exists
     */
    fun hasAttribute(name: String): Boolean = attributes.containsKey(name)

    /**
     * Shortcut function for setting the xmlns:
     * namespace attribute
     *
     * @param name The namespace name
     * @param value The value of the namespace
     */
    fun namespace(name: String, value: String) {
        attribute("xmlns:$name", value)
    }

    // endregion

    // region Operator Overloads

    /**
     * Operator overloading for accessing attributes using
     * the: this["name"] syntax
     *
     * @see attribute The underlying implementation
     * @param attributeName The name of the attribute
     * @return The attribute value or null
     */
    operator fun get(attributeName: String): Any? = attribute(attributeName)

    /**
     * Operator overloading for setting attributes using
     * the: this["name] = value syntax
     *
     * @see attribute The underlying implementation
     * @param attributeName The attribute name
     * @param attributeValue The new value for the attribute
     */
    operator fun set(attributeName: String, attributeValue: Any?) {
        attribute(attributeName, attributeValue)
    }

    /**
     * Operator overloading for creating text nodes using the "invoke"
     * syntax: "NodeName"("NodeValue")
     *
     * For this operator the "this" value is the node name
     *
     * @see textNode The underlying implementation
     * @param value The text value
     */
    operator fun String.invoke(value: String) = textNode(this, value)

    /**
     * Operator overloading for creating text nodes using the "invoke"
     * syntax: "NodeName"("NodeValue")
     *
     * For this operator the "this" value is the node name
     *
     * @see textNode The underlying implementation
     * @param value The text value
     */
    operator fun String.invoke(value: Any?) = textNode(this, value)

    /**
     * Operator overloading for creating nodes using the "invoke" syntax: "NodeName" { }
     * Creates a new node adding it to the children and providing it as the receiver
     *
     * @param init The function which creates the node tree
     * @receiver The node that was created
     */
    inline operator fun String.invoke(init: XmlNode.() -> Unit) {
        val xmlNode = node(this)
        xmlNode.init()
    }

    /**
     * Operator overloading for creating comment nodes
     * using the unaryMinus syntax
     */
    operator fun String.unaryMinus() = comment(this)

    /**
     * Operator overloading for creating text nodes
     * using the unaryPlus syntax
     */
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
                outputOptions.appendEscapedValue(builder, textElement.text)
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

    /**
     * Checks if the content is only made up of
     * a single ignorable value
     *
     * @return Whether the content is only a single ignorable value
     */
    private fun isOnlyIgnorable(): Boolean {
        return children.size == 1 && children[0] is XmlIgnorable
    }

    /**
     * Checks to see if the children list is empty or
     * if there is only an ignorable element left that
     * is allowed to be ignored.
     *
     * This function is used to determine whether the tag
     * can be closed straight away without rendering content
     *
     * @return Whether the content is empty
     */
    private fun isContentEmpty(): Boolean {
        if (children.isEmpty()) return true
        if (!isOnlyIgnorable()) return false

        val ignorableElement = children[0] as XmlIgnorable
        return ignorableElement.isIgnorable()
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