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
import com.liferay.apio.consumer.request.RequestAuthorization
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.request.RequestExecutor
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import okhttp3.Authenticator
import okhttp3.HttpUrl
import java.lang.Exception

/**
 * @author Paulo Cruz
 */
class ApioConsumer @JvmOverloads constructor(authenticator: Authenticator? = null) {
    init {
        RequestAuthorization.authenticator = authenticator
    }

    @JvmOverloads
    fun fetch(url: HttpUrl, fields: Map<String, List<String>> = emptyMap(), embedded: List<String> = emptyList(),
        onSuccess: (Thing) -> Unit, onError: (Exception) -> Unit) {

        launch(UI) {
            withContext(CommonPool) {
                try {
                    Result.of(RequestExecutor.requestThing(url, fields, embedded))
                } catch (e: Exception) {
                    Result.error(e)
                }
            }.fold(onSuccess, onError)
        }
    }

    @JvmOverloads
    fun performOperation(thingId: String, operationId: String,
        fillFields: (List<Property>) -> Map<String, Any> = { emptyMap() },
        onSuccess: (Thing) -> Unit, onError: (Exception) -> Unit) {

        launch(UI) {
            withContext(CommonPool) {
                try {
                    Result.of(RequestExecutor.performOperation(thingId, operationId, fillFields))
                } catch (e: Exception) {
                    Result.error(e)
                }
            }.fold(onSuccess, onError)
        }
    }

    @JvmOverloads
    fun setAuthenticator(authenticator: Authenticator? = null) {
        RequestAuthorization.authenticator = authenticator
    }

    internal fun requestProperties(url: String, onSuccess: (List<Property>) -> Unit, onError: (Exception) -> Unit) {
        launch(UI) {
            withContext(CommonPool) {
                try {
                    Result.of(RequestExecutor.requestProperties(url))
                } catch (e: Exception) {
                    Result.error(e)
                }
            }.fold(onSuccess, onError)
        }
    }
}
