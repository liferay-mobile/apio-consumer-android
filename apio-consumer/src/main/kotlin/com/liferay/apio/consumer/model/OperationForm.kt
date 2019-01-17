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
import com.liferay.apio.consumer.parser.ThingParser
import kotlinx.android.parcel.Parcelize

/**
 * @author Javier Gamarra
 */
@Parcelize
data class OperationForm(val id: String, val title: String, val description: String?, val properties: List<Property>)
    : Parcelable {

    companion object {
        val converter: (Thing) -> OperationForm = { it: Thing ->
            val title = it["title"] as String
            val description = it["description"] as? String
            val supportedProperty = it["supportedProperty"] as List<Map<String, Any>>

            val properties = supportedProperty.map { getProperty(it) }

            OperationForm(it.id, title, description, properties)
        }

        private fun getProperty(propertyMap: Map<String, Any>): Property {
            val type = ThingParser.parseType(propertyMap["@type"])
            val name = propertyMap["property"] as String
            val required = propertyMap["required"] as Boolean

            return Property(type, name, required)
        }
    }
}