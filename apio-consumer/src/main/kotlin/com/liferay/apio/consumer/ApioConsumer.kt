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

import com.liferay.apio.consumer.configuration.RequestConfiguration
import com.liferay.apio.consumer.configuration.RequestHeader
import com.liferay.apio.consumer.configuration.merge
import com.liferay.apio.consumer.extensions.asHttpUrl
import com.liferay.apio.consumer.model.OperationForm
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.request.RequestExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Javier Gamarra
 * @author Paulo Cruz
 */
class ApioConsumer constructor(val defaultHeaders: List<RequestHeader>) {

	constructor(vararg defaultHeaders: RequestHeader?) : this(defaultHeaders.filterNotNull())

	@JvmOverloads
	fun fetchResource(thingId: String, configs: RequestConfiguration = RequestConfiguration(),
		onComplete: (Result<Thing>) -> Unit) {

		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				runCatching {
					val headers = defaultHeaders.merge(configs.headers)
					val url = thingId.asHttpUrl()

					RequestExecutor.requestThing(url, configs.fields, configs.embedded, headers)
				}
			}.also(onComplete)
		}
	}

	@JvmOverloads
	fun getOperationForm(operationExpects: String, configs: RequestConfiguration = RequestConfiguration(),
		onComplete: (Result<List<Property>>) -> Unit) {

		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				runCatching {
					val headers = defaultHeaders.merge(configs.headers)
					val url = operationExpects.asHttpUrl()

					val operationForm = RequestExecutor.requestThing(url, headers = headers).let {
						OperationForm.converter(it)
					}

					operationForm.properties
				}
			}.also(onComplete)
		}
	}

	@JvmOverloads
	fun performOperation(thingId: String, operationId: String, configs: RequestConfiguration = RequestConfiguration(),
		fillFields: (List<Property>) -> Map<String, Any> = emptyFillFields(), onComplete: (Result<Thing>) -> Unit) {

		GlobalScope.launch(Dispatchers.Main) {
			withContext(Dispatchers.IO) {
				runCatching {
					val headers = defaultHeaders.merge(configs.headers)

					RequestExecutor.performOperation(thingId, operationId, headers, fillFields)
				}
			}.also(onComplete)
		}
	}

	private fun emptyFillFields() = { _: List<Property> -> emptyMap<String, Any>() }
}
