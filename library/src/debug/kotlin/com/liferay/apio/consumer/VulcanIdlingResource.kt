package com.liferay.apio.consumer

import android.support.test.espresso.IdlingResource

object ApioIdlingResources : IdlingResource {

	var idle = true
	var resourceCallback: IdlingResource.ResourceCallback? = null

	override fun getName() = ApioIdlingResources::class.java.name

	override fun registerIdleTransitionCallback(
		resourceCallback: IdlingResource.ResourceCallback) {
		this.resourceCallback = resourceCallback
	}

	override fun isIdleNow(): Boolean {
		if (idle && resourceCallback != null) {
			resourceCallback?.onTransitionToIdle()
		}
		return idle
	}

}
