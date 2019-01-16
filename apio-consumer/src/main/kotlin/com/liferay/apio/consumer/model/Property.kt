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
data class Property(val type: ThingType, val name: String, val required: Boolean) : Parcelable {

    companion object {
        val converter: (Map<String, Any>) -> Property = { it: Map<String, Any> ->
            val type = ThingParser.parseType(it["@type"])
            val name = it["property"] as String
            val required = it["required"] as Boolean
            Property(type, name, required)
        }
    }
}
