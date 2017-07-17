package com.liferay.vulcan.consumer.extensions

import java.text.DateFormat
import java.util.Date
import java.util.Locale

fun Date.shortFormat(locale: Locale = Locale.US) = format(DateFormat.getDateInstance(DateFormat.SHORT, locale))

fun Date.mediumFormat(locale: Locale = Locale.US) = format(DateFormat.getDateInstance(DateFormat.MEDIUM, locale))

fun Date.longFormat(locale: Locale = Locale.US) = format(DateFormat.getDateInstance(DateFormat.LONG, locale))

fun Date.fullFormat(locale: Locale = Locale.US) = format(DateFormat.getDateInstance(DateFormat.FULL, locale))

fun Date.format(format: DateFormat): String = format.format(this)