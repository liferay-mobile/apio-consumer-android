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

package com.liferay.apio.consumer.parser

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liferay.apio.consumer.exception.CantParseToThingException
import com.liferay.apio.consumer.graph.ApioGraph
import com.liferay.apio.consumer.model.*
import okhttp3.Response

/**
 * @author Paulo Cruz
 */
class ThingParser {

    companion object {
        @Throws(CantParseToThingException::class)
        fun parse(response: Response): Thing {
            return response.body()?.let {
                parse(it.string())
            }?.let {
                val (thing, embeddedThings) = it
                ApioGraph.updateNodes(thing, embeddedThings)

                if (!response.isSuccessful) {
                    throw CantParseToThingException()
                }

                thing
            } ?: throw CantParseToThingException()
        }

        fun parseType(type: Any?): ThingType {
            return (type as? String)?.let { listOf(it) }
                ?: type as? ThingType
                ?: listOf()
        }

        @Throws(JsonParseException::class, JsonSyntaxException::class)
        internal fun parse(json: String): Pair<Thing, Map<String, Thing?>>? {
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

                val thing = Thing(id, types, attributes, operations = operations)

                return thing to things
            }

            return null
        }

        private fun foldEntry(context: Context?) = { acc: FoldedAttributes, entry: Map.Entry<String, Any> ->
            val (attributes, things) = acc

            val key = entry.key
            val value = entry.value

            when {
                value is Embedded<*, *> -> value.parse(context, things, attributes, key)
                value is EmbeddedList<*> && value.hasMapInstances() ->
                    value.parse(context, things, attributes, key)
                context != null && context.isId(key) -> with(value as String) {
                    things[this] = null
                    attributes[key] = Relation(this)
                }
                else -> attributes[key] = value
            }

            attributes to things
        }

        private fun foldTree(jsonObject: Map<String, Any>,
            context: Context?): Pair<MutableMap<String, Any>, MutableMap<String, Thing?>> {
            return jsonObject
                .filter { it.key !in listOf("@id", "@type", "@context") }
                .entries
                .fold(mutableMapOf<String, Any>() to mutableMapOf(), foldEntry(context))
        }

        private fun Embedded<*, *>.parse(context: Context?, things: MutableMap<String, Thing?>,
            attributes: MutableMap<String, Any>, key: String) {

            (this as? Embedded<String, Any>)?.apply {
                if (this.containsKey("@id")) {
                    val result = flatten(this, context)

                    result?.let {
                        val (thing, embeddedThings) = it
                        attributes[key] = Relation(thing.id)

                        things.put(thing.id, thing)

                        things.putAll(embeddedThings)
                    }
                } else {
                    val (attr, embeddedThings) = foldTree(this, context)

                    things.putAll(embeddedThings)
                    attributes[key] = attr
                }
            }
        }

        private fun EmbeddedList<*>.parse(context: Context?, things: MutableMap<String, Thing?>,
            attributes: MutableMap<String, Any>, key: String) {

            (this as? EmbeddedList<Embedded<String, Any>>)?.apply {
                if (this.first().containsKey("@id")) {
                    val list = this.map { flatten(it, context)!! }
                    val mutableList = mutableListOf<Relation>()

                    for ((thing, embeddedThings) in list) {
                        mutableList.add(Relation(thing.id))

                        things.put(thing.id, thing)

                        things.putAll(embeddedThings)
                    }

                    attributes[key] = mutableList
                } else {
                    val pairsList = this.map { foldTree(it, context) }

                    val foldedList = mutableListOf<Map<String, Any>>()

                    for ((list, embeddedThings) in pairsList) {
                        foldedList.add(list)

                        things.putAll(embeddedThings)
                    }

                    attributes[key] = foldedList
                }
            }
        }

        private fun EmbeddedList<*>.hasMapInstances() = !this.filterIsInstance<Map<String, Any>>().isEmpty()

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
    }

}

typealias Embedded<T, U> = Map<T, U>
typealias EmbeddedList<T> = List<T>
typealias FoldedAttributes = Pair<MutableMap<String, Any>, MutableMap<String, Thing?>>
