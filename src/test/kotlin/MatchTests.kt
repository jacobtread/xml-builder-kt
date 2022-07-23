import com.jacobtread.xml.XmlVersion
import com.jacobtread.xml.element.XmlTextElement
import com.jacobtread.xml.xml
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MatchTests {

    private fun assertOutputEqual(expected: String, actualResult: String) {
        val a = expected.replace("\r\n", "\n").trim()
        val b = actualResult.replace("\r\n", "\n").trim()
        assertEquals(a, b)
    }

    @Test
    fun `test create using operator standard`() {
        val expectedResult = """
            <Test>
            	<InnerElement>
            		<!-- This is a commented out line -->
            		Text String Line
            		Another String Line
            		And Another line
            	</InnerElement>
            	<SingleTest>This is some content</SingleTest>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            node("InnerElement") {
                comment("This is a commented out line")
                text("Text String Line")
                text("Another String Line")
                text("And Another line")
            }
            textNode("SingleTest", "This is some content")
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test create other types`() {
        val expectedResult = """
            <Test>
            	<InnerElement>
            		<![CDATA[This is CDATA]]>
            		Text String Line
            		<?test?>
            		<?test1 a="b"?>
            		<?test2 c="d"?>
            	</InnerElement>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            node("InnerElement") {
                cdata("This is CDATA")
                text("Text String Line")
                processingInstruction("test")
                processingInstruction("test1", "a" to "b")
                processingInstruction("test2", mapOf("c" to "d"))
            }
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test global processing instruction`() {
        val expectedResult = """
            <?test?>
            <?test1 a="b"?>
            <?test2 c="d"?>
            <Test>
            	<InnerElement>
            		<![CDATA[This is CDATA]]>
            		Text String Line
            	</InnerElement>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            globalProcessingInstruction("test")
            globalProcessingInstruction("test1", "a" to "b")
            globalProcessingInstruction("test2", mapOf("c" to "d"))
            node("InnerElement") {
                cdata("This is CDATA")
                text("Text String Line")
            }
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test xmlns`() {
        val expectedResult = """
            <Test xmlns="value"/>
        """.trimIndent()
        val result = xml("Test") {
            xmlns = "value"
        }
        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test append to builder`() {
        val expectedResult = """
            <Test xmlns="value"/>
        """.trimIndent()
        val result = xml("Test") {
            xmlns = "value"
        }
        val builder = StringBuilder()
        result.appendToBuilder(builder)
        val actualResult = builder.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test namespace`() {
        val expectedResult = """
            <Test xmlns:type="value"/>
        """.trimIndent()
        val result = xml("Test") {
            namespace("type", "value")
        }
        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `check has attribute`() {
        xml("Test") {
            // First way
            attribute("test", "value")

            // Second way
            this["test1"] = "value1"

            // Third
            set("test2", "value2")

            assertTrue(hasAttribute("test"))
            assertTrue(hasAttribute("test1"))
            assertTrue(hasAttribute("test2"))
        }
    }

    @Test
    fun `check has matches`() {
        xml("Test") {
            // First way
            attribute("test", "value")

            // Second way
            this["test1"] = "value1"

            // Third
            set("test2", "value2")

            assertEquals(attribute("test"), "value")
            assertEquals(this["test1"], "value1")
            assertEquals(get("test2"), "value2")
        }
    }

    @Test
    fun `test create using operator overloading`() {
        val expectedResult = """
            <Test>
            	<InnerElement>
            		<!-- This is a commented out line -->
            		Text String Line
            		Another String Line
            		And Another line
            	</InnerElement>
            	<SingleTest>This is some content</SingleTest>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            "InnerElement" {
                -"This is a commented out line"
                +"Text String Line"
                +"Another String Line"
                +"And Another line"
            }
            "SingleTest"("This is some content")
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test add element`() {
        val expectedResult = """
            <Test>Text String Line</Test>
        """.trimIndent()

        val result = xml("Test") {
            addElement(XmlTextElement("Text String Line"))
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test non string node operator overload`() {
        val expectedResult = """
            <Test>
            	<SingleTest>3</SingleTest>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            "SingleTest"(3)
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test doctype`() {
        val expectedResult = """
            <!DOCTYPE test PUBLIC "test" "test">
            <Test/>
        """.trimIndent()

        val result = xml("Test") {
            doctype("test", "test", "test")
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test non string node`() {
        val expectedResult = """
            <Test>
            	<SingleTest>3</SingleTest>
            </Test>
        """.trimIndent()

        val result = xml("Test") {
            textNode("SingleTest", 3)
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test create with version`() {
        val expectedResult = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Test/>
        """.trimIndent()

        val result = xml("Test") {
            version = XmlVersion.V10
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

    @Test
    fun `test create with attributes`() {
        val expectedResult = """
            <?xml version="1.0" encoding="UTF-8"?>
            <Test test="value" test1="value1" test2="value2"/>
        """.trimIndent()

        val result = xml("Test") {
            version = XmlVersion.V10

            // First way
            attribute("test", "value")

            // Second way
            this["test1"] = "value1"

            // Third
            set("test2", "value2")
        }

        val actualResult = result.toString()
        assertOutputEqual(expectedResult, actualResult)
    }

}