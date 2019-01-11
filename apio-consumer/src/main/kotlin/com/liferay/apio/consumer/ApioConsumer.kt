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

package com.liferay.apio.consumer

import com.github.kittinunf.result.Result
import com.liferay.apio.consumer.configuration.*
import com.liferay.apio.consumer.exception.InvalidRequestUrlException
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.request.*
import kotlinx.coroutines.*
import okhttp3.HttpUrl

/**
 * @author Javier Gamarra
 * @author Paulo Cruz
 */
class ApioConsumer constructor(val defaultHeaders: List<RequestHeader>) {

	constructor(vararg defaultHeaders: RequestHeader?) : this(defaultHeaders.filterNotNull())

	@JvmOverloads
	fun fetchResource(thingId: String, configs: RequestConfiguration = RequestConfiguration(),
		onComplete: (Result<Thing, Exception>) -> Unit) {

		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				try {
					val headers = defaultHeaders.merge(configs.headers)
					val url = HttpUrl.parse(thingId) ?: throw InvalidRequestUrlException()

					Result.of(RequestExecutor.requestThing(url, configs.fields, configs.embedded, headers))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	@JvmOverloads
	fun performOperation(thingId: String, operationId: String, configs: RequestConfiguration = RequestConfiguration(),
		fillFields: (List<Property>) -> Map<String, Any> = emptyFillFields(),
		onComplete: (Result<Thing, Exception>) -> Unit) {

		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				try {
					val headers = defaultHeaders.merge(configs.headers)

					Result.of(RequestExecutor.performOperation(thingId, operationId, headers, fillFields))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	internal fun requestProperties(operationId: String, onComplete: (Result<List<Property>, Exception>) -> Unit) {
		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				try {
					Result.of(RequestExecutor.requestProperties(operationId))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	private fun emptyFillFields() = { _: List<Property> -> emptyMap<String, Any>() }
}
