package com.liferay.apio.consumer.extensions

import android.os.Build
import java.util.*

/**
 * @author Paulo Cruz
 */
fun Locale.toW3cLanguageId(): String {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		this.toLanguageTag()
	} else {
		this.displayLanguage.replace("_", "-")
	}
}