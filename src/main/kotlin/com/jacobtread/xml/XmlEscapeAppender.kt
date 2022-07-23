package com.jacobtread.xml

interface XmlEscapeAppender {

    fun appendEscapedValue(builder: Appendable, value: String)

}