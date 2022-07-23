package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

/**
 * Represents an element that is a part of a xml tree which
 * can be rendered out onto the final xml representation
 */
interface XmlElement {

    fun render(builder: Appendable, indent: String, outputOptions: OutputOptions)

    fun isEmpty(): Boolean
}