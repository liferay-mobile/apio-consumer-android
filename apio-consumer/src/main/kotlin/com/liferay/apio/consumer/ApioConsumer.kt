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
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.liferay.apio.consumer.authenticator.ApioAuthenticator
import com.liferay.apio.consumer.model.Property
import com.liferay.apio.consumer.model.Thing
import com.liferay.apio.consumer.request.RequestAuthorization
import com.liferay.apio.consumer.request.RequestExecutor
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import okhttp3.HttpUrl

/**
 * @author Javier Gamarra
 * @author Paulo Cruz
 */
class ApioConsumer @JvmOverloads constructor(authenticator: ApioAuthenticator? = null) {

	init {
		RequestAuthorization.authenticator = authenticator
	}

	fun fetch(url: HttpUrl, authenticator: ApioAuthenticator? = null, fields: Map<String, List<String>> = emptyMap(),
		embedded: List<String> = emptyList(), onSuccess: (Thing) -> Unit,
		onError: (Exception) -> Unit = emptyOnError()) {

		fetch(url, authenticator, fields, embedded) {
			it.fold(onSuccess, onError)
		}
	}

	@JvmOverloads
	fun fetch(url: HttpUrl, authenticator: ApioAuthenticator? = null, fields: Map<String, List<String>> = emptyMap(),
		embedded: List<String> = emptyList(), onComplete: (Result<Thing, Exception>) -> Unit = emptyOnComplete()) {

		authenticator?.run {
			setAuthenticator(authenticator)
		}

		launch(UI) {
			withContext(CommonPool) {
				try {
					Result.of(RequestExecutor.requestThing(url, fields, embedded))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	fun performOperation(thingId: String, operationId: String, authenticator: ApioAuthenticator? = null,
		fillFields: (List<Property>) -> Map<String, Any> = emptyFillFields(),
		onSuccess: (Thing) -> Unit, onError: (Exception) -> Unit = emptyOnError()) {

		performOperation(thingId, operationId, authenticator, fillFields) {
			it.fold(onSuccess, onError)
		}
	}

	@JvmOverloads
	fun performOperation(thingId: String, operationId: String, authenticator: ApioAuthenticator? = null,
		fillFields: (List<Property>) -> Map<String, Any> = emptyFillFields(),
		onComplete: (Result<Thing, Exception>) -> Unit = emptyOnComplete()) {

		authenticator?.run {
			setAuthenticator(authenticator)
		}

		launch(UI) {
			withContext(CommonPool) {
				try {
					Result.of(RequestExecutor.performOperation(thingId, operationId, fillFields))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	@JvmOverloads
	fun setAuthenticator(authenticator: ApioAuthenticator? = null) {
		RequestAuthorization.authenticator = authenticator
	}

	internal fun requestProperties(url: String, onComplete: (Result<List<Property>, Exception>) -> Unit) {
		launch(UI) {
			withContext(CommonPool) {
				try {
					Result.of(RequestExecutor.requestProperties(url))
				} catch (e: Exception) {
					Result.error(e)
				}
			}.also(onComplete)
		}
	}

	private fun emptyOnComplete() = { _: Result<Thing, Exception> -> }
	private fun emptyOnError() = { _: Exception -> }
	private fun emptyFillFields() = { _: List<Property> -> emptyMap<String, Any>() }
}
