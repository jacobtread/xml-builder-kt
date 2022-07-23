package com.jacobtread.xml

/**
 * Immutable set of options which is passed to the render functions
 * to influence how the content is rendered

 * @constructor Create Output options with default values
 */
data class OutputOptions(
    /**
     * Determines whether new lines should be used. If this is false
     * no indentation or new lines are used and the xml nodes are
     * all rendered to the same line
     */
    val prettyPrint: Boolean = true,
    /**
     * Whether to print nodes that only have a single text element
     * on the same line they will look like the following:
     *
     * ```
     * <element>Text Content</element>
     * ```
     *
     * Instead of
     *
     * ```
     * <element>
     *     Text Content
     * </element>
     * ```
     */
    val singleLineTextElements: Boolean = true,
    /**
     * Determines whether self-closing tags will be used on
     * elements that have none / empty children
     *
     * ```
     * <element/>
     * ```
     *
     * instead of
     *
     * ```
     * <element></element>
     * ```
     */
    val useSelfClosingTags: Boolean = true,
    /**
     * Determines whether to use escaped character or character reference
     *
     * When enabled " becomes `&#39;` and when disabled " becomes &apos;
     */
    val useCharacterReferences: Boolean = false,
    /**
     * The character used for indentation. Each level of indentation will
     * append this character on to create an additional indentation level
     */
    val indent: String = "\t",
) {
    /**
     * Represents the current XML version. This is set by the root xml node at
     * the start of the render.
     */
    internal var xmlVersion: XmlVersion = XmlVersion.V10

    /**
     * The character used to end a line. If pretty print is enabled this is
     * the system line separator otherwise its empty
     */
    internal val lineEnding = if (prettyPrint) System.lineSeparator() else ""

    /**
     * Appends the escaped version of the provided value
     * to the provided [builder]
     *
     * @param builder The builder to append to
     * @param value The value to escape
     */
    internal fun appendEscapedValue(
        builder: Appendable,
        value: Any?,
    ) {
        val stringValue = value?.toString()
        if (stringValue == null) {
            builder.append(null)
        } else if (useCharacterReferences) {
            stringValue.forEach { char ->
                when (char) {
                    '\'' -> builder.append("&#39;")
                    '&' -> builder.append("&#38;")
                    '<' -> builder.append("&#60;")
                    '>' -> builder.append("&#62;")
                    '"' -> builder.append("&#34;")
                    else -> builder.append(char)
                }
            }
        } else {
            xmlVersion.appendEscapedValue(builder, stringValue)
        }
    }

}
