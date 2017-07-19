package com.liferay.vulcan.consumer.screens.events

import android.view.View
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.Scenario
import com.liferay.vulcan.consumer.screens.views.BaseView

sealed class Event<T> {
	class Click(val view: View, val thing: Thing) : Event<View.OnClickListener>()

	class FetchLayout(val view: BaseView? = null, val thing: Thing, val scenario: Scenario) : Event<Int>()
}