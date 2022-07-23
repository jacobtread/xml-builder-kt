package com.jacobtread.xml

enum class XmlVersion(val versionNumber: String) {
    V10("1.0") {
        override fun appendEscapedValue(builder: Appendable, value: String) {
            value.forEach {
                when (it.code) {
                    34 /* " */ -> builder.append("&quot;")
                    38 /* & */ -> builder.append("&amp;")
                    60 /* < */ -> builder.append("&lt;")
                    62 /* > */ -> builder.append("&gt;")
                    39 /* ' */ -> builder.append("&apos;")
                    in 0..8,
                    0xb, 0xc, 0xe, 0xf, 0xfffe, 0xffff,
                    in 0x10..0x19,
                    in 0x1a..0x1f,
                    in Char.MIN_SURROGATE.code..Char.MAX_SURROGATE.code,
                    -> return@forEach
                    in 0x7f..0x84,
                    in 0x86..0x9f,
                    -> builder.append("&#").append(it.code.toString(10)).append(';')
                    else -> builder.append(it)
                }
            }
        }
    },
    V11("1.1") {
        override fun appendEscapedValue(builder: Appendable, value: String) {
            value.forEach {
                when (it.code) {
                    34 /* " */ -> builder.append("&quot;")
                    38 /* & */ -> builder.append("&amp;")
                    60 /* < */ -> builder.append("&lt;")
                    62 /* > */ -> builder.append("&gt;")
                    39 /* ' */ -> builder.append("&apos;")
                    0, 0xfffe, 0xffff,
                    in Char.MIN_SURROGATE.code..Char.MAX_SURROGATE.code,
                    -> return@forEach
                    in 0x1..0x8,
                    in 0xb..0xc,
                    in 0xe..0x1f,
                    in 0x7f..0x84,
                    in 0x86..0x9f,
                    -> builder.append("&#").append(it.code.toString(10)).append(';')
                    else -> builder.append(it)
                }
            }
        }
    };

    abstract fun appendEscapedValue(builder: Appendable, value: String)

}