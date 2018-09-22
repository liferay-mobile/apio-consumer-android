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

package com.liferay.apio.consumer.model

import android.os.Parcelable
import com.liferay.apio.consumer.ApioConsumer
import com.liferay.apio.consumer.exception.ApioException
import com.liferay.apio.consumer.graph.ApioGraph
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.lang.Exception

typealias ThingType = List<String>

@Parcelize
data class Thing(val id: String, val type: ThingType, val attributes: Map<String, @RawValue Any>,
    val name: String? = null, val operations: MutableMap<String, Operation> = mutableMapOf()) : Parcelable

@Parcelize
data class Relation(val id: String) : Parcelable

@Parcelize
data class Operation(val id: String, val target: String, val type: ThingType, val method: String,
    var form: OperationForm?) : Parcelable

@Parcelize
data class OperationForm(val id: String, var properties: List<Property> = listOf()) : Parcelable

@Parcelize
data class Property(val type: ThingType, val name: String, val required: Boolean) : Parcelable

data class Context(val vocab: String, val attributeContext: Map<String, Any>)

fun contextFrom(jsonObject: List<Any>?, parentContext: Context?): Context? {
    return jsonObject?.let {
        val vocab =
            it.find { it is Map<*, *> && it["@vocab"] is String }
                .let { (it as? Map<*, *>)?.get("@vocab") as? String }
                ?: parentContext?.vocab
                ?: throw ApioException("Empty Vocab")

        val attributeContexts = HashMap<String, Any>()

        it
            .filter { it is Map<*, *> }
            .forEach {
                (it as? Map<String, Any>)?.filterKeys { it != "@vocab" }?.let {
                    attributeContexts.putAll(it)
                }
            }

        Context(vocab, attributeContexts)
    }
}

fun Context.isId(attributeName: String): Boolean =
    (attributeContext[attributeName] as? Map<String, Any>)
        ?.let { it["@type"] }
        ?.let { it == "@id" }
        ?: false

operator fun Thing.get(attribute: String): Any? = attributes[attribute]

operator fun Relation.get(attribute: String): Any? = ApioGraph.graph[id]?.value?.get(attribute)

fun Thing.merge(value: Thing?): Thing = value?.let { Thing(id, type, attributes + it.attributes) } ?: this

fun Thing.containsOperation(operationId: String): Boolean = operations.keys.none { it.contains(operationId) }

fun Thing.getOperation(operationId: String): Operation? {
	val key = operations.keys.firstOrNull { it.contains(operationId) }

	return key?.let { operations[it] }
}

fun OperationForm.getFormProperties(onSuccess: (List<Property>) -> Unit, onError: (Exception) -> Unit) {
	ApioConsumer().requestProperties(id, {
		this.properties = it

        onSuccess(it)
	}, onError)
}



