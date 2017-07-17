package com.liferay.vulcan.consumer.screens

import android.view.View
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.BaseView

sealed class Event<T>

class ClickEvent(val view: View, val thing: Thing) : Event<View.OnClickListener>()

class GetLayoutEvent(val view: BaseView? = null, val thing: Thing, val scenario: Scenario) : Event<Int>()

interface Scenario {
    companion object {
        var stringToScenario: ((String) -> Scenario?)? = null
    }
}

object Detail : Scenario

object Row: Scenario

data class Custom(val name: String): Scenario

interface ScreenletEvents {
    fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing): View.OnClickListener? = null
    fun <T : BaseView> onGetCustomLayout(
        screenlet: ThingScreenlet, parentView: T?, thing: Thing, scenario: Scenario): Int? = null
}