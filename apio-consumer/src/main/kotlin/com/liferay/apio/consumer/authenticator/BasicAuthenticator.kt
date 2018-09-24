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

package com.liferay.apio.consumer.authenticator

import okhttp3.Request

/**
 * @author Paulo Cruz
 */
class BasicAuthenticator(private val credentials: String?) : ApioAuthenticator {

    override fun authenticate(request: Request): Request {
        return request.newBuilder()
            .addHeader(AUTHORIZATION_KEY, credentials)
            .build()
    }

    companion object {
        private const val AUTHORIZATION_KEY: String = "Authorization"
    }

}