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

package com.liferay.apio.consumer.request

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.apio.consumer.cache.ThingsCache
import com.liferay.apio.consumer.cache.ThingsCache.get
import com.liferay.apio.consumer.exception.*
import com.liferay.apio.consumer.extensions.asHttpUrl
import com.liferay.apio.consumer.model.OperationForm
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.parser.ThingParser
import com.liferay.apio.consumer.util.RequestUtil
import okhttp3.*
import okhttp3.internal.http.HttpMethod
import java.io.IOException

/**
 * @author Javier Gamarra
 */
internal class RequestExecutor {

	companion object {
		@Throws(ApioException::class, CantParseToThingException::class, InvalidRequestUrlException::class,
			IOException::class, JsonSyntaxException::class, RequestFailedException::class,
			ThingNotFoundException::class, ThingWithoutOperationException::class)
		fun performOperation(thingId: String, operationId: String, headers: Map<String, String>,
			fillFields: (List<Property>) -> Map<String, Any> = { emptyMap() }): Thing {

			val thing = ThingsCache[thingId]?.value
				?: throw ThingNotFoundException()

			val operation = thing.operations[operationId]
				?: throw ThingWithoutOperationException(thingId, operationId)

			if (operation.expects != null && operation.form == null) {
				operation.form = requestThing(operation.expects.asHttpUrl(), headers = headers).let {
					OperationForm.converter(it)
				}
			}

			val attributes = operation.form?.let {
				fillFields(it.properties)
			} ?: emptyMap()

			val response = requestOperation(operation.method, operation.target, Headers.of(headers), attributes)

			return checkResponseStatus(response)
		}

		@Throws(ApioException::class, CantParseToThingException::class, IOException::class, JsonSyntaxException::class,
			RequestFailedException::class, ThingNotFoundException::class)
		fun requestThing(url: HttpUrl, fields: Map<String, List<String>> = emptyMap(),
			embedded: List<String> = emptyList(), headers: Map<String, String> = emptyMap()): Thing {

			val httpUrl = RequestUtil.createUrl(url, fields, embedded)

			val response = request(httpUrl, Headers.of(headers))

			return checkResponseStatus(response)
		}

		@Throws(ApioException::class, JsonSyntaxException::class, RequestFailedException::class,
			ThingNotFoundException::class)
		private fun checkResponseStatus(response: Response): Thing {
			if (response.isSuccessful) {
				return ThingParser.parse(response)
			} else {
				throw RequestUtil.getResponseException(response)
			}
		}

		@Throws(IOException::class)
		private fun execute(request: Request): Response {
			val okHttpClient = OkHttpClient.Builder().build()

			return okHttpClient.newCall(request).execute()
		}

		@Throws(IOException::class)
		private fun request(httpUrl: HttpUrl, headers: Headers = Headers.of()): Response {
			val request = RequestUtil.createRequest(httpUrl, headers)

			return execute(request)
		}

		@Throws(IOException::class, InvalidRequestUrlException::class)
		private fun requestOperation(method: String, target: String, headers: Headers = Headers.of(),
			attributes: Map<String, Any> = emptyMap()): Response {

			val requestBody = attributes.let {
				if (attributes.isEmpty() && !HttpMethod.permitsRequestBody(method)) {
					null
				} else {
					RequestUtil.getRequestBody(attributes)
				}
			}

			val url = target.asHttpUrl()
			val request = RequestUtil.createRequest(url, headers, method, requestBody)

			return RequestExecutor.execute(request)
		}
	}

}