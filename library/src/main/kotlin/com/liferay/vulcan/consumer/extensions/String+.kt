package com.liferay.vulcan.consumer.extensions

import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.asDate(format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)): Date? =
    try { format.parse(this) } catch (parseException: ParseException) { null }

fun String.md5(): String {
    return MessageDigest.getInstance("MD5").run {
        update(this@md5.toByteArray(Charset.defaultCharset()))
        digest()
    }.let {
        it.fold(StringBuilder()) { builder, byte ->
            builder.append(byte.toHexString())
        }
    }.toString()
}