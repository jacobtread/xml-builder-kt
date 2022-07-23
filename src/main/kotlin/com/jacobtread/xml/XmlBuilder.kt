package com.jacobtread.xml

import com.jacobtread.xml.element.XmlRootNode

inline fun xml(nodeName: String, init: XmlRootNode.() -> Unit): XmlRootNode {
    val rootNode = XmlRootNode(nodeName)
    rootNode.init()
    return rootNode
}

