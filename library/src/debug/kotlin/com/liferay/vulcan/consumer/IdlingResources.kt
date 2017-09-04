package com.liferay.vulcan.consumer

import android.support.test.espresso.IdlingPolicies
import android.support.test.espresso.IdlingRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object IdlingResources {

	fun registerOkHttp(okHttpClient: OkHttpClient, name: String) {
		val create = OkHttp3IdlingResource.create("_", okHttpClient)
		IdlingRegistry.getInstance().register(create)
	}

	fun unregisterOkHttp(okHttpClient: OkHttpClient, name: String) {
		val create = OkHttp3IdlingResource.create("_", okHttpClient)
		IdlingRegistry.getInstance().unregister(create)
	}

	private var idlingResource: VulcanIdlingResources? = null

	fun getServer() {

	}

	fun setPolicy() {
		val l: Long = 5
		IdlingPolicies.setIdlingResourceTimeout(l, TimeUnit.MINUTES)
		IdlingPolicies.setMasterPolicyTimeout(l, TimeUnit.MINUTES)
	}

}
