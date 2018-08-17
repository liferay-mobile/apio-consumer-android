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

import android.os.Parcel
import android.os.Parcelable
import com.liferay.apio.consumer.ApioException
import com.liferay.apio.consumer.graph
import com.liferay.apio.consumer.requestProperties

typealias ThingType = List<String>

data class Thing(val id: String, val type: ThingType, val attributes: Map<String, Any>, val name: String? = null,
    val operations: MutableMap<String, Operation> = mutableMapOf()) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList(),
        mutableMapOf(),
        parcel.readString(),
        mutableMapOf()) {

        parcel.readMap(attributes, Any::class.java.classLoader)
        parcel.readMap(operations, Operation::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeStringList(type)
        parcel.writeMap(attributes)
        parcel.writeString(name)
        parcel.writeMap(operations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thing> {
        override fun createFromParcel(parcel: Parcel): Thing {
            return Thing(parcel)
        }

        override fun newArray(size: Int): Array<Thing?> {
            return arrayOfNulls(size)
        }
    }
}

data class Relation(val id: String)

data class Operation(val id: String, val target: String, val type: ThingType, val method: String,
    var form: OperationForm?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readParcelable(OperationForm::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(target)
        parcel.writeStringList(type)
        parcel.writeString(method)
        parcel.writeParcelable(form, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Operation> {
        override fun createFromParcel(parcel: Parcel): Operation {
            return Operation(parcel)
        }

        override fun newArray(size: Int): Array<Operation?> {
            return arrayOfNulls(size)
        }
    }
}

data class OperationForm(val id: String, var properties: List<Property> = listOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Property))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeTypedList(properties)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OperationForm> {
        override fun createFromParcel(parcel: Parcel): OperationForm {
            return OperationForm(parcel)
        }

        override fun newArray(size: Int): Array<OperationForm?> {
            return arrayOfNulls(size)
        }
    }
}

data class Property(val type: ThingType, val name: String, val required: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(type)
        parcel.writeString(name)
        parcel.writeByte(if (required) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Property> {
        override fun createFromParcel(parcel: Parcel): Property {
            return Property(parcel)
        }

        override fun newArray(size: Int): Array<Property?> {
            return arrayOfNulls(size)
        }
    }
}

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
				.forEach({
					(it as? Map<String, Any>)?.filterKeys { it != "@vocab" }?.let {
						attributeContexts.putAll(it)
					}
				})

		Context(vocab, attributeContexts)
	}
}

fun Context.isId(attributeName: String): Boolean =
		(attributeContext[attributeName] as? Map<String, Any>)
				?.let { it["@type"] }
				?.let { it == "@id" }
				?: false

operator fun Thing.get(attribute: String): Any? = attributes[attribute]

operator fun Relation.get(attribute: String): Any? = graph[id]?.value?.get(attribute)

fun Thing.merge(value: Thing?): Thing = value?.let { Thing(id, type, attributes + it.attributes) } ?: this

fun Thing.containsOperation(operationId: String): Boolean = operations.keys.filter { it.contains(operationId) }.isEmpty()

fun Thing.getOperation(operationId: String): Operation? {
	val key = operations.keys.filter { it.contains(operationId) }.firstOrNull()

	return key?.let { operations[it] }
}

fun OperationForm.getFormProperties(onComplete: (List<Property>) -> Unit) {
	requestProperties(id) {
		this.properties = it

		onComplete(it)
	}
}



