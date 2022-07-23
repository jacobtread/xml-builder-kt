package com.jacobtread.xml

import com.jacobtread.xml.element.XmlRootNode

/**
 * This functions creates a root xml node for building a xml tree
 *
 * @param nodeName The name of the root node
 * @param init The function for building the tree
 * @receiver The created root node
 * @return The created root node
 */
inline fun xml(nodeName: String, init: XmlRootNode.() -> Unit): XmlRootNode {
    val rootNode = XmlRootNode(nodeName)
    rootNode.init()
    return rootNode
}

