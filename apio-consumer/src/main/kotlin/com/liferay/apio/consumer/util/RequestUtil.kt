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

package com.liferay.apio.consumer.util

import com.google.gson.Gson
import com.liferay.apio.consumer.authenticator.ApioAuthenticator
import com.liferay.apio.consumer.exception.ApioException
import com.liferay.apio.consumer.exception.RequestFailedException
import com.liferay.apio.consumer.exception.ThingNotFoundException
import com.liferay.apio.consumer.parser.ThingParser
import com.liferay.apio.consumer.parser.ThingParser.Companion.toJsonMap
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URLConnection

/**
 * @author Javier Gamarra
 * @author Paulo Cruz
 */
internal class RequestUtil {

	companion object {
		fun createRequest(httpUrl: HttpUrl, method: String? = null, requestBody: RequestBody? = null,
			authenticator: ApioAuthenticator? = null): Request {

			val request = Request.Builder()
				.url(httpUrl)
				.addHeader("Accept", "application/ld+json")
				.apply {
					method?.let {
						this.method(method, requestBody)
					}
				}
				.build()

			return authenticator?.authenticate(request) ?: request
		}

		fun createUrl(url: HttpUrl, fields: Map<String, List<String>>, embedded: List<String>): HttpUrl {
			return url.newBuilder()
				.apply {
					fields.forEach { (type, values) ->
						val types = values.joinToString(separator = ",")

						this.addQueryParameter("fields[$type]", types)
					}
				}
				.addQueryParameter("embedded", embedded.joinToString(","))
				.build()
		}

		fun getRequestBody(attributes: Map<String, Any>): RequestBody {
			if (attributes.values.none { it is InputStream }) {
				val json = Gson().toJson(attributes)

				return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
			} else {
				val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

				for ((key, value) in attributes) {
					if (value is InputStream) {
						val byteArray = getByteArrayFromInputStream(value)
						val contentType = getContentType(attributes, value)
						val body = RequestBody.create(MediaType.parse(contentType), byteArray)

						builder.addFormDataPart(key, key, body)
					} else {
						builder.addFormDataPart(key, value as String)
					}
				}

				return builder.build()
			}
		}

		fun getResponseException(response: Response): Exception {
			return response.body()?.string()?.toJsonMap()?.let {
				RequestFailedException(
					it["statusCode"] as Number,
					ThingParser.parseType(it["@type"]),
					it["title"] as String,
					it["description"] as? String
				)
			} ?: response.message().takeIf {
				it.isNotEmpty()
			}?.let {
				ApioException(it)
			} ?: ThingNotFoundException()
		}

		private fun getByteArrayFromInputStream(inputStream: InputStream): ByteArray {
			val byteBuffer = ByteArrayOutputStream()

			inputStream.use { input ->
				byteBuffer.use { output ->
					input.copyTo(output)
				}
			}

			return byteBuffer.toByteArray()
		}

		private fun getContentType(attributes: Map<String, Any>, inputStream: InputStream): String {
			val name = attributes["name"] as? String

			return URLConnection.guessContentTypeFromStream(inputStream)
				?: URLConnection.guessContentTypeFromName(name)
				?: "application/*"
		}
	}
}
