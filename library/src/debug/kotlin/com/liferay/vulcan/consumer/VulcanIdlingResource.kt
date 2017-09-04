package com.liferay.vulcan.consumer

import android.support.test.espresso.IdlingResource

class VulcanIdlingResources : IdlingResource {

	var idle: Boolean = true
	var resourceCallback: IdlingResource.ResourceCallback? = null

	override fun getName() = VulcanIdlingResources::class.java.name

	override fun registerIdleTransitionCallback(
		resourceCallback: IdlingResource.ResourceCallback) {
		this.resourceCallback = resourceCallback
	}

	override fun isIdleNow(): Boolean {
		if (idle) {
			resourceCallback?.onTransitionToIdle()
		}
		return idle
	}

}
