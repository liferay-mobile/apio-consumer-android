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
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.success
import com.liferay.apio.consumer.ApioConsumer
import kotlinx.android.parcel.Parcelize

/**
 * @author Javier Gamarra
 */
@Parcelize
data class OperationForm(val id: String, var properties: List<Property> = listOf()) : Parcelable {

	fun getFormProperties(onSuccess: (List<Property>) -> Unit, onError: (Exception) -> Unit) {
		getFormProperties {
			it.fold(onSuccess, onError)
		}
	}

	fun getFormProperties(onComplete: (Result<List<Property>, Exception>) -> Unit) {
		ApioConsumer().requestProperties(id) {
			it.success { properties ->
				this.properties = properties
			}

			onComplete(it)
		}
	}

}
