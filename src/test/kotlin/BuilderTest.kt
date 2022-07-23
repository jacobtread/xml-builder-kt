import com.jacobtread.xml.element.XmlRootNode

fun test() {

    val root = XmlRootNode("Test")

    root.node("Test") {

        "" {
            "" {

            }

            ""()
        }

        attribute("", "")

        node("", "")
    }

}