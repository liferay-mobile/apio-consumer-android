package com.liferay.vulcan.consumer

import android.support.test.espresso.IdlingResource

object VulcanIdlingResources : IdlingResource {

	var idle = true
	var resourceCallback: IdlingResource.ResourceCallback? = null

	override fun getName() = VulcanIdlingResources::class.java.name

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
