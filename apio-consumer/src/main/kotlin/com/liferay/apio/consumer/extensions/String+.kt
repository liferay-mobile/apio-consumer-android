/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.consumer.extensions

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.apio.consumer.exception.InvalidRequestUrlException
import okhttp3.HttpUrl
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.asDate(format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)): Date? =
	try {
		format.parse(this)
	} catch (parseException: ParseException) {
		null
	}

fun String.asHttpUrl(): HttpUrl = HttpUrl.parse(this) ?: throw InvalidRequestUrlException()

private val gson = Gson()
private val mapType = TypeToken.getParameterized(Map::class.java, String::class.java, Any::class.java).type

@Throws(JsonSyntaxException::class)
fun String.asJsonMap(): Map<String, Any>? = gson.fromJson(this, mapType)