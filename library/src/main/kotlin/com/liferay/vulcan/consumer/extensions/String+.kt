package com.liferay.vulcan.consumer.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.asDate(format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)): Date? =
	try { format.parse(this) } catch (parseException: ParseException) { null }