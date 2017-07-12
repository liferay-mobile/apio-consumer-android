package com.liferay.vulcan.consumer.extensions

private val CHARS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun Byte.toHexString() : String {
    val i = this.toInt()

    val char2 = CHARS[i and 0x0f]

    val char1 = CHARS[i shr 4 and 0x0f]

    return "$char1$char2"
}