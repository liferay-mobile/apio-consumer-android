package com.liferay.vulcan.consumer.screens

import android.view.View
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ViewScenario.DETAIL
import com.liferay.vulcan.consumer.screens.views.BaseView

sealed class Action<T>

class ClickAction(val view: View, val thing: Thing) : Action<View.OnClickListener>()

class CustomLayoutAction(val view: BaseView? = null, val thing: Thing, val scenario: ViewScenario = DETAIL) : Action<Int>()

enum class ViewScenario {
    ROW, DETAIL
}

interface ScreenletEvents {
    fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing): View.OnClickListener? = null
    fun <T : BaseView> onGetCustomLayout(screenlet: ThingScreenlet, parentView: T?, thing: Thing, scenario: ViewScenario): Int? = null
}