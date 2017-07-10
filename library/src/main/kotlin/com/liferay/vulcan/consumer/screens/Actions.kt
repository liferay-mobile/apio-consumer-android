package com.liferay.vulcan.consumer.screens

import android.view.View
import com.liferay.vulcan.consumer.model.Thing

sealed class Action<T>

class ClickAction(val view: View, val thing: Thing) : Action<View.OnClickListener>()

enum class ViewScenario {
    ROW, DETAIL
}
