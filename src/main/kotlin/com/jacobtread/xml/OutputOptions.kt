package com.jacobtread.xml

data class OutputOptions(
    val prettyPrint: Boolean = true,
    val singleLineTextElements: Boolean = false,
    val useSelfClosingTags: Boolean = true,
    val useCharacterReferences: Boolean = false,
    val indent: String = "\t",
) {
    internal var xmlVersion: XmlVersion = XmlVersion.V10
}
