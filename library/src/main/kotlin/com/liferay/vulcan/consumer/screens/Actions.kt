package com.liferay.vulcan.consumer.screens

import android.view.View
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.Scenario.DETAIL
import com.liferay.vulcan.consumer.screens.adapter.ThingViewHolder
import com.liferay.vulcan.consumer.screens.views.BaseView

sealed class Event<T>

class ClickEvent(val view: View, val thing: Thing) : Event<View.OnClickListener>()

class GetLayoutEvent(val view: BaseView? = null, val thing: Thing, val scenario: Scenario) : Event<ViewInfo>()

enum class Scenario {
    DETAIL, ROW
}

sealed class ViewInfo(val id: Int)

class Detail(id: Int) : ViewInfo(id)
class Row(id: Int, val viewHolderCreator: (View, ThingViewHolder.Listener) -> ThingViewHolder) : ViewInfo(id)

interface ScreenletEvents {
    fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing): View.OnClickListener? = null
    fun <T : BaseView> onGetCustomLayout(
        screenlet: ThingScreenlet, parentView: T?, thing: Thing, scenario: Scenario): ViewInfo? = null
}