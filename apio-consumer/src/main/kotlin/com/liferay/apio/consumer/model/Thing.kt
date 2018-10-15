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

/**
 * @author Javier Gamarra
 */
typealias ThingType = List<String>

@Parcelize
data class Thing(val id: String, val type: ThingType, val attributes: Map<String, @RawValue Any>,
	val name: String? = null, val operations: MutableMap<String, Operation> = mutableMapOf()) : Parcelable {

	fun merge(value: Thing?): Thing = value?.let { Thing(id, type, attributes + it.attributes) } ?: this

	fun containsOperation(operationId: String): Boolean = operations.keys.none { it.contains(operationId) }

	fun getOperation(operationId: String): Operation? {
		val key = operations.keys.firstOrNull { it.contains(operationId) }

		return key?.let { operations[it] }
	}
}

operator fun Thing.get(attribute: String): Any? = attributes[attribute]
