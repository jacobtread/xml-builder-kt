package com.jacobtread.xml.element

import com.jacobtread.xml.OutputOptions
import com.jacobtread.xml.XmlVersion

class XmlRootNode(nodeName: String) : XmlNode(nodeName) {

    private var includeXmlProlog = false

    var docType: DoctypeElement? = null

    var encoding: String = "UTF-8"
        set(value) {
            includeXmlProlog = true
            field = value
        }

    var version: XmlVersion = XmlVersion.V10
        set(value) {
            includeXmlProlog = true
            field = value
        }

    var standalone: Boolean? = null
        set(value) {
            includeXmlProlog = true
            field = value
        }


    override fun render(builder: Appendable, indent: String, outputOptions: OutputOptions) {
        // Extra beforehand rendering
        super.render(builder, indent, outputOptions)
    }

}