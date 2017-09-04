package com.liferay.vulcan.consumer

import android.support.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient

object IdlingResources {

	private val idlingResource: VulcanIdlingResources = VulcanIdlingResources()

	private val idlingRegistry = IdlingRegistry.getInstance()

	fun registerOkHttp(okHttpClient: OkHttpClient, name: String) {
		val create = OkHttp3IdlingResource.create(name, okHttpClient)
		idlingRegistry.register(create)
	}

	fun register() {
		idlingRegistry.register(idlingResource)
		idlingResource.idle = false
	}

	fun unregister() {
		idlingResource.resourceCallback?.onTransitionToIdle()
		idlingResource.idle = true
	}
}
