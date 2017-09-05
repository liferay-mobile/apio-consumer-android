package com.liferay.vulcan.consumer

import okhttp3.OkHttpClient

object IdlingResources {
	fun registerOkHttp(okHttpClient: OkHttpClient, name: String) {
		println("registerOkHttp empty implementation")
	}

	fun unregisterOkHttp(okHttpClient: OkHttpClient, name: String) {
		println("unregisterOkHttp empty implementation")
	}

}
