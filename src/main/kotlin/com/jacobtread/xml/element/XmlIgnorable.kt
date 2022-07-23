package com.jacobtread.xml.element

/**
 * Represents an element that can have its rendering
 * ignore when certain conditions are met e.g. empty string
 */
interface XmlIgnorable {

    fun isIgnorable(): Boolean

}