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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liferay.apio.consumer.model.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import java.io.IOException
import kotlin.collections.Map.Entry

fun fetch(
	url: HttpUrl, credentials: String? = null, fields: Map<String, List<String>> = emptyMap(),
	embedded: List<String> = emptyList(),
	onComplete: (Result<Thing, Exception>) -> Unit) {

	launch(UI) {
		async(CommonPool) {
			requestParseWaitLoop(url, fields, embedded, credentials)
		}.await().let(onComplete)
	}
}

fun performOperationAndParse(thingId: String, operationId: String,
 	fillFields: (List<Property>) -> Map<String, Any> = { emptyMap() },
 	onComplete: (Result<Thing, Exception>) -> Unit) {

	performOperation(thingId, operationId, fillFields) {
		it.component1()?.let {
			launch(UI) {
				async(CommonPool) {
					parse(it)
				}.await().let(onComplete)
			}
		} ?: onComplete(Result.of(null, { ApioException("No thing returned") }))
	}
}

fun performOperation(thingId: String, operationId: String,
	fillFields: (List<Property>) -> Map<String, Any> = { emptyMap() },
	onComplete: (Result<Response, Exception>) -> Unit) {

	val thing = graph[thingId]?.value

	thing?.let {
		val operation = thing.operations[operationId]

		operation?.let {
			operation.form?.let { form ->
				if (form.properties.isEmpty()) {
					requestProperties(form.id) {
						form.properties = it
						thing.operations[operationId] = operation

						val attributes = fillFields(it)

						performOperationRequest(operation.target, operation.method, attributes, onComplete)
					}
				}
				else {
					val attributes = fillFields(form.properties)

					performOperationRequest(operation.target, operation.method, attributes, onComplete)
				}
			} ?: performOperationRequest(operation.target, operation.method, emptyMap(), onComplete)

		} ?: onComplete(Result.of(null, { ApioException("Thing $it doesn't have the operation $operationId") }))

	} ?: onComplete(Result.of(null, { ApioException("Thing not found") }))
}

fun performOperationRequest(url: String, method: String, attributes: Map<String, Any>,
	onComplete: (Result<Response, Exception>) -> Unit) {
	launch(UI) {
		async(CommonPool) {

			val json = Gson().toJson(attributes)
			val request = createRequest(HttpUrl.parse(url), credential).newBuilder()
				.method(method, RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
				.build()

			val okHttpClient = OkHttpClient()

			try {
				Result.of(okHttpClient.newCall(request).execute())
			} catch (e: Exception) {
				Result.error(e)
			}
		}.await().let(onComplete)
	}
}

fun requestProperties(url: String, onComplete: (List<Property>) -> Unit) {
	val request = createRequest(HttpUrl.parse(url), credential)

	launch(UI) {
		async(CommonPool) {
			val okHttpClient = OkHttpClient()
			val response = okHttpClient.newCall(request).execute()

			val json = response.body()?.string()

			val mapType = TypeToken.getParameterized(Map::class.java, String::class.java, Any::class.java).type

			val jsonObject = Gson().fromJson<Map<String, Any>>(json, mapType)

			val supportedProperties = jsonObject["supportedProperty"] as List<Map<String, Any>>

			supportedProperties.map {
				val type = parseType(it["@type"])
				val name = it["property"] as String
				val required = it["required"] as Boolean
				Property(type, name, required)
			}
		}.await().let(onComplete)
	}
}

fun requestParseWaitLoop(url: HttpUrl,
	fields: Map<String, List<String>>,
	embedded: List<String>,
	credentials: String?): Result<Thing, Exception> {
	return try {
		val response = request(url, fields, embedded, credentials)
		parse(response)
	} catch (e: IOException) {
		Result.error(e)
	}
}

private fun request(url: HttpUrl,
	fields: Map<String, List<String>>,
	embedded: List<String>, credentials: String?): Response {
	val httpUrl = createUrl(url, fields, embedded)

	if (credentials != null) {
		credential = credentials
	}

	val request = createRequest(httpUrl, credential)

	val okHttpClient = OkHttpClient()

	val execute = okHttpClient.newCall(request).execute()

	return execute
}

private fun createRequest(httpUrl: HttpUrl?, credential: String?): Request =
	Request.Builder()
		.url(httpUrl)
		.addHeader("Authorization", credential)
		.addHeader("Accept", "application/ld+json")
		.build()

private fun createUrl(url: HttpUrl, fields: Map<String, List<String>>, embedded: List<String>): HttpUrl =
	url
		.newBuilder()
		.apply {
			fields.forEach { (type, values) ->
				val types = values.joinToString(separator = ",")

				this.addQueryParameter("fields[$type]", types)
			}
		}
		.addQueryParameter("embedded", embedded.joinToString(","))
		.build()

private fun parse(
	response: Response): Result<Thing, Exception> =
	response.body()?.let {
		val result = parse(it.string())

		result?.let {
			val (thing, embeddedThings) = it
			val nodes = embeddedThings.map { (id, embeddedThing) ->
				val previousThing = graph[id]?.value

				val newThing = embeddedThing?.merge(previousThing) ?: previousThing

				id to Node(id, newThing)
			}

			graph.put(thing.id, Node(thing.id, thing))
			graph.putAll(nodes)

            Result.of(thing)
        }
    } ?: Result.of(null, { ApioException("Not Found") })

class Node(val id: String, var value: Thing? = null)

var graph: MutableMap<String, Node> = mutableMapOf()

var credential: String = Credentials.basic("test@liferay.com", "test")

fun parse(json: String): Pair<Thing, Map<String, Thing?>>? {
	val mapType = TypeToken.getParameterized(Map::class.java, String::class.java, Any::class.java).type

	val jsonObject = Gson().fromJson<Map<String, Any>>(json, mapType)

	return flatten(jsonObject, null)
}

private fun flatten(jsonObject: Map<String, Any>, parentContext: Context?): Pair<Thing, Map<String, Thing?>>? {

	if (!jsonObject.containsKey("statusCode")) {
		val id = jsonObject["@id"] as String

		val types = parseType(jsonObject["@type"])

		val context = contextFrom(jsonObject["@context"] as? List<Any>, parentContext)

		val (attributes, things) = foldTree(jsonObject, context)

		val operations = parseOperations(jsonObject)

		val thing = Thing(id, types, attributes, operations =  operations)

		return thing to things
	}
	return null
}

private fun parseOperations(jsonObject: Map<String, Any>): MutableMap<String, Operation> {
	val operationsJson = jsonObject["operation"] as? List<Map<String, Any>> ?: listOf()

	return operationsJson.map {
		val id = it["@id"] as String
		val target = it["target"] as String
		val method = it["method"] as String
		val expects = it["expects"] as? String
		val type = parseType(it["@type"])

		val form = expects?.let { OperationForm(it) }

		id to Operation(id, target, type, method, form)
	}.toMap().toMutableMap()
}

private fun parseType(type: Any?): Type {
	return (type as? String)?.let { listOf(it) }
		?: type as? Type
		?: listOf()
}


private fun foldTree(jsonObject: Map<String, Any>,
	context: Context?): Pair<MutableMap<String, Any>, MutableMap<String, Thing?>> {
	return jsonObject
		.filter { it.key !in listOf("@id", "@type", "@context") }
		.entries
		.fold(mutableMapOf<String, Any>() to mutableMapOf(), foldEntry(context))
}


typealias FoldedAttributes = Pair<MutableMap<String, Any>, MutableMap<String, Thing?>>

private fun foldEntry(context: Context?) = { acc: FoldedAttributes, entry: Entry<String, Any> ->
	val (attributes, things) = acc

	val key = entry.key
	val value = entry.value

	when {
		value is Map<*, *> -> (value as? Map<String, Any>)?.apply {
			if (this.containsKey("@id")) {
				val result = flatten(this, context)

				result?.let {
					val (thing, embeddedThings) = it
					attributes[key] = Relation(thing.id)

					things.put(thing.id, thing)

					things.putAll(embeddedThings)
				}
			}
			else {
				val (attr, embeddedThings) = foldTree(this, context)

				things.putAll(embeddedThings)
				attributes[key] = attr
			}
		}
		value is List<*> && !value.filterIsInstance<Map<String, Any>>().isEmpty() ->
			(value as? List<Map<String, Any>>)?.apply {

				if (this.first().containsKey("@id")) {
					val list = this.map { flatten(it, context)!! }
					val mutableList = mutableListOf<Relation>()

					for ((thing, embeddedThings) in list) {
						mutableList.add(Relation(thing.id))

						things.put(thing.id, thing)

						things.putAll(embeddedThings)
					}

					attributes[key] = mutableList
				}
				else {
					val pairsList = this.map { foldTree(it, context) }

					val foldedList = mutableListOf<Map<String, Any>>()

					for ((list, embeddedThings) in pairsList) {
						foldedList.add(list)

						things.putAll(embeddedThings)
					}

					attributes[key] = foldedList
				}

			}
		context != null && context.isId(key) -> with(value as String) {
			things[this] = null

			attributes[key] = Relation(this)
		}
		else -> attributes[key] = value
	}

	attributes to things
}

class ApioException(s: String) : Exception(s)
