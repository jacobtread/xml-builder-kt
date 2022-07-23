package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions

class XmlTextElement(val text: String) : XmlElement {

    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
    }

    override fun isEmpty(): Boolean {
        return text.isEmpty()
    }
}