package com.liferay.vulcan.consumer.screens.events

import android.view.View
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.Scenario

interface ScreenletEvents {
    fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing): View.OnClickListener? = null
    fun <T : BaseView> onGetCustomLayout(
        screenlet: ThingScreenlet, parentView: T?, thing: Thing, scenario: Scenario): Int? = null
}