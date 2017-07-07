package com.liferay.vulcan.consumer.extensions

import java.text.DateFormat
import java.util.*

fun Date.shortFormat() = format(DateFormat.getDateInstance(DateFormat.SHORT, Locale.US))

fun Date.mediumFormat() = format(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US))

fun Date.longFormat() = format(DateFormat.getDateInstance(DateFormat.LONG, Locale.US))

fun Date.fullFormat() = format(DateFormat.getDateInstance(DateFormat.FULL, Locale.US))

fun Date.format(format: DateFormat): String = format.format(this)