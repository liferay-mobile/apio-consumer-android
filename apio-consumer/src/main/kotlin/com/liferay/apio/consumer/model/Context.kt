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

/**
 * @author Javier Gamarra
 */
data class Context(val vocab: String, val attributeContext: Map<String, Any>) {

    fun isId(attributeName: String): Boolean {
        return (attributeContext[attributeName] as? Map<String, Any>)
            ?.let { it["@type"] }
            ?.let { it == "@id" }
            ?: false
    }

}