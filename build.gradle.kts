plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

group = "com.jacobtread.xml"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    repositories {
        maven {
            name = "Sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val sonatypeUser: String? by project
            val sonatypeKey: String? by project
            credentials {
                username = sonatypeUser
                password = sonatypeKey
            }
        }
    }

    publications {
        register<MavenPublication>("sonatype") {
            from(components["java"])

            pom {
                name.set("XML Builder KT")
                description.set("XML Typesafe builder library for Kotlin")
                url.set("https://github.com/jacobtread/xml-builder-kt")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/jacobtread/xml-builder-kt/blob/master/LICENSE.md")
                    }
                }

                developers {
                    developer {
                        id.set("jacobtread")
                        name.set("Jacobtread")
                        email.set("jacobtread@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/jacobtread/xml-builder-kt")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}