package com.jacobtread.xml.element

interface XmlEscapeAppender {

    fun appendEscapedValue(builder: Appendable, value: String)

}