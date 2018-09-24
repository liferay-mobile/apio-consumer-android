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
import com.google.gson.reflect.TypeToken
import com.liferay.apio.consumer.authenticator.ApioAuthenticator
import com.liferay.apio.consumer.exception.CantParseToThingException
import com.liferay.apio.consumer.exception.InvalidRequestUrlException
import com.liferay.apio.consumer.exception.ThingNotFoundException
import com.liferay.apio.consumer.exception.ThingWithoutOperationException
import com.liferay.apio.consumer.graph.ApioGraph
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.parser.ThingParser
import com.liferay.apio.consumer.util.RequestUtil
import okhttp3.*
import okhttp3.internal.http.HttpMethod
import java.io.IOException
import java.lang.Exception

/**
 * @author Paulo Cruz
 */
internal class RequestExecutor {

    companion object {
        @Throws(CantParseToThingException::class, IOException::class, ThingNotFoundException::class,
            ThingWithoutOperationException::class)
        fun performOperation(thingId: String, operationId: String,
            fillFields: (List<Property>) -> Map<String, Any> = { emptyMap() }): Thing {

            val thing = ApioGraph.graph[thingId]?.value
                ?: throw ThingNotFoundException()

            val operation = thing.operations[operationId]
                ?: throw ThingWithoutOperationException(thingId, operationId)

            var attributes = emptyMap<String, Any>()

            if(operation.form != null) {
                val form = operation.form!!

                if (form.properties.isEmpty()) {
                    form.properties = requestProperties(form.id)
                    thing.operations[operationId] = operation
                }

                attributes = fillFields(form.properties)
            }

            val response = requestOperation(operation.target, operation.method, attributes)
            return ThingParser.parse(response)
        }

        @Throws(CantParseToThingException::class, IOException::class)
        fun requestThing(url: HttpUrl, fields: Map<String, List<String>>, embedded: List<String>): Thing {
            val httpUrl = RequestUtil.createUrl(url, fields, embedded)
            val response = request(httpUrl)

            return ThingParser.parse(response)
        }

        @Throws(IOException::class)
        internal fun requestProperties(url: String): List<Property> {
            val httpUrl = HttpUrl.parse(url) ?: throw InvalidRequestUrlException()

            val response = request(httpUrl)

            val json = response.body()?.string()

            val mapType = TypeToken.getParameterized(Map::class.java, String::class.java, Any::class.java).type

            val jsonObject = Gson().fromJson<Map<String, Any>>(json, mapType)

            val supportedProperties = jsonObject["supportedProperty"] as List<Map<String, Any>>

            return supportedProperties.map {
                val type = ThingParser.parseType(it["@type"])
                val name = it["property"] as String
                val required = it["required"] as Boolean
                Property(type, name, required)
            }
        }

        @Throws(IOException::class)
        private fun execute(request: Request, authenticator: ApioAuthenticator?): Response {
            val authenticatedRequest = authenticator?.authenticate(request) ?: request
            val okHttpClient = OkHttpClient.Builder().build()

            return okHttpClient.newCall(authenticatedRequest).execute()
        }

        @Throws(IOException::class)
        private fun request(httpUrl: HttpUrl): Response {
            val request = RequestUtil.createRequest(httpUrl)

            return execute(request, RequestAuthorization.authenticator)
        }

        @Throws(IOException::class)
        private fun requestOperation(url: String, method: String, attributes: Map<String, Any> = emptyMap())
            : Response {

            val requestBody = attributes.let {
                if(attributes.isEmpty() && !HttpMethod.permitsRequestBody(method)) {
                    null
                } else {
                    RequestUtil.getRequestBody(attributes)
                }
            }

            val request = RequestUtil.createRequest(HttpUrl.parse(url), method, requestBody)

            return RequestExecutor.execute(request, RequestAuthorization.authenticator)
        }
    }

}