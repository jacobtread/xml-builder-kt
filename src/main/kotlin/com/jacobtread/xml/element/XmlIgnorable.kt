package com.jacobtread.xml.element

/**
 * Represents an element that can have its rendering
 * ignore when certain conditions are met e.g. empty string
 */
interface XmlIgnorable {

    /**
     * Implemented by elements the result of this determines whether
     * the render for this element will be skipped and whether the
     * closing tag of the parent can be merged
     *
     * @return Whether this element can be ignored
     */
    fun isIgnorable(): Boolean

}