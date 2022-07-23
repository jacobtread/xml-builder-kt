# Kotlin XML Builder

![Latest Version](https://img.shields.io/maven-central/v/com.jacobtread.xml/xml-builder-kt?label=LATEST%20VERSION&style=for-the-badge)

This library is a Kotlin library for composing XML documents using a kotlin DSL.

> Note: The published releases of this library are compiled for Java 11 Bytecode if you need to use
> a lower version of Java you will need to modify `javaCompileVersion` in `gradle.properties`
> and change it to your desired version

### Using this dependency

Maven

```xml
<dependency>
    <groupId>com.jacobtread.xml</groupId>
    <artifactId>xml-builder-kt</artifactId>
    <version>{VERSION}</version>
</dependency>
```

Groovy

```groovy
dependencies {
    implementation 'com.jacobtread.xml:xml-builder-kt:{VERSION}'
}
```

Kotlin DSL

```kotlin
dependencies {
    implementation("com.jacobtread.xml:xml-builder-kt:{VERSION}")
}
```

### Operator Overloads

- Unary minus operator `-"Something"` - This turn any strings provided into comment elements
- Unary plus operator `+"Something"` - This turns any strings provided into text elements
- String invoke Paren `("Something")` - This creates a new text node with the provided strings
- String invoke Curly `{}` - This creates a new XmlNode and applies its context to between the curly braces

## Examples

> Note: Setting the version, encoding, or standalone values will result in xml prolog being generated
> you can manually enable prolog generation by setting `includeXmlProlog` to `true`

### Basic XML Example

The following code block produces a simple XML structure with a comment

```kotlin
import com.jacobtread.xml.xml

val result = xml("Test") {
    "InnerElement" {
        -"This is a commented out line"
        +"Text String Line"
        +"Another String Line"
        +"And Another line"
    }
    "SingleTest"("This is some content")
}

val stringResult = result.toString()
println(stringResult)
```

This code produces the following output

```xml

<Test>
    <InnerElement>
        <!-- This is a commented out line -->
        Text String Line
        Another String Line
        And Another line
    </InnerElement>
    <SingleTest>This is some content</SingleTest>
</Test>
```

### XML Version Example

The following example is the same as the previous except this time the XML version is explicitly
stated causing a xml prolog to be generated

```kotlin
import com.jacobtread.xml.xml
import com.jacobtread.xml.XmlVersion

val result = xml("Test") {
    version = XmlVersion.V10

    "InnerElement" {
        -"This is a commented out line"
        +"Text String Line"
        +"Another String Line"
        +"And Another line"
    }
    "SingleTest"("This is some content")
}

val stringResult = result.toString()
println(stringResult)
```

This code produces the following output

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Test>
    <InnerElement>
        <!-- This is a commented out line -->
        Text String Line
        Another String Line
        And Another line
    </InnerElement>
    <SingleTest>This is some content</SingleTest>
</Test>
```

### XML Attributes

There is 3 different ways of setting attributes on XmlNodes, and they are the
following

```kotlin
val result = xml("Test") {
    // First way
    attribute("test", "value")

    // Second way
    this["test1"] = "value1"

    // Third
    set("test2", "value2")
}

val stringResult = result.toString()
println(stringResult)
```

This code produces the following output

```xml

<Test test="value" test1="value1" test2="value2"/>
```