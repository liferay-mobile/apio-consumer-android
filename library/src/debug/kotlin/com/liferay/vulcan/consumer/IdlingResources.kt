package com.liferay.vulcan.consumer

import android.support.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient

object IdlingResources {
	fun registerOkHttp(okHttpClient: OkHttpClient) {
		val create = OkHttp3IdlingResource.create("okhttp", okHttpClient)
		IdlingRegistry.getInstance().register(create)
	}
}
