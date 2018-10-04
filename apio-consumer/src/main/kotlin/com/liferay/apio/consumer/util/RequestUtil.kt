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
import com.liferay.apio.consumer.exception.ThingNotFoundException
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.model.get
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URLConnection

/**
 * @author Paulo Cruz
 */
class RequestUtil {

    companion object {
        fun createRequest(httpUrl: HttpUrl?, authenticator: ApioAuthenticator?): Request {
            val request = Request.Builder()
                .url(httpUrl!!)
                .addHeader("Accept", "application/ld+json")
                .build()

            return authenticator?.authenticate(request) ?: request
        }

        fun createRequest(httpUrl: HttpUrl?, method: String, requestBody: RequestBody?,
            authenticator: ApioAuthenticator?): Request {

            return createRequest(httpUrl, authenticator)
                .newBuilder()
                .method(method, requestBody)
                .build()
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

        fun getResponseException(thing: Thing, response: Response): Exception {
            return thing.let {
                it["title"] as? String
            }?.let {
                ApioException(it)
            } ?: run {
                if (response.message().isNotEmpty()) {
                    ApioException(response.message())
                } else {
                    ThingNotFoundException()
                }
            }
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
