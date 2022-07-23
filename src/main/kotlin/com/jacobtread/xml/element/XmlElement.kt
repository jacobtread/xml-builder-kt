package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Represents an element that is a part of a xml tree which
 * can be rendered out onto the final xml representation
 */
sealed interface XmlElement {

    /**
     * Function implemented by each element which handles converting
     * the xml element into a string representation
     *
     * @param builder The string builder to append to
     * @param indent The current line indentation (none when pretty printing)
     * @param outputOptions The output settings
     */
    fun render(builder: Appendable, indent: String, outputOptions: OutputOptions)
}