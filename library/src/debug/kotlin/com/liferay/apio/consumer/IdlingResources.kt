package com.liferay.apio.consumer

import android.support.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient

object IdlingResources {

	fun registerOkHttp(okHttpClient: OkHttpClient, name: String) {
		val create = OkHttp3IdlingResource.create("_", okHttpClient)
		IdlingRegistry.getInstance().register(create)
	}

	fun unregisterOkHttp(okHttpClient: OkHttpClient, name: String) {
//		val create = OkHttp3IdlingResource.create("_", okHttpClient)
//		IdlingRegistry.getInstance().unregister(create)
	}

}
