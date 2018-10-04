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

package com.liferay.apio.consumer.exception

/**
 * @author Paulo Cruz
 */
open class ApioException(s: String) : Exception(s)

class CantParseToThingException : ApioException("Can't parse to thing")

class InvalidRequestUrlException : ApioException("Invalid request URL")

class ThingNotFoundException : ApioException("Thing not found")

class ThingWithoutOperationException(thingId: String, operationId: String)
    : ApioException("Thing $thingId doesn't have the operation $operationId")
