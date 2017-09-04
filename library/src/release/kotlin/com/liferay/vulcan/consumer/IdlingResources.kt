package com.liferay.vulcan.consumer

import okhttp3.OkHttpClient

object IdlingResources {
	fun registerOkHttp(okHttpClient: OkHttpClient, name: String) {
		println("registerOkHttp empty implementation")
	}

	fun register() {
		println("register empty implementation")
	}

	fun unregister() {
		println("unregister empty implementation")
	}
}
