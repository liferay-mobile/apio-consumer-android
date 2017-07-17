package com.liferay.vulcan.consumer.screens

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.github.kittinunf.result.failure
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.observe
import com.liferay.vulcan.consumer.screens.events.ClickEvent
import com.liferay.vulcan.consumer.screens.events.Event
import com.liferay.vulcan.consumer.screens.events.GetLayoutEvent
import com.liferay.vulcan.consumer.screens.events.ScreenletEvents
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Person
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.Custom
import com.liferay.vulcan.consumer.screens.views.Detail
import com.liferay.vulcan.consumer.screens.views.Row
import com.liferay.vulcan.consumer.screens.views.Scenario
import okhttp3.HttpUrl

open class BaseScreenlet @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    var layout: View? = null
}

class ThingScreenlet @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    BaseScreenlet(context, attrs, defStyleAttr, defStyleRes) {

    var screenletEvents: ScreenletEvents? = null

    var scenario: Scenario = Detail

    companion object {
        val layoutIds: MutableMap<String, MutableMap<Scenario, Int>> = mutableMapOf(
            "BlogPosting" to BlogPosting.DEFAULT_VIEWS,
            "Collection" to Collection.DEFAULT_VIEWS,
            "Person" to Person.DEFAULT_VIEWS
        )
    }

    val layoutId: Int

    var thing: Thing? by observe {
        val layoutId = getLayoutIdFor(thing = it) ?: R.layout.thing_default

        layout?.also {
            this.removeView(it)
        }

        layout = this.inflate(layoutId)

        addView(layout)

        (layout as? BaseView)?.apply {
            screenlet = this@ThingScreenlet
            thing = it
        }
    }

    val viewModel: ViewModel? get() = layout as? ViewModel

    fun load(thingId: String, scenario: Scenario? = null, onComplete: ((ThingScreenlet) -> Unit)? = null) {
        fetch(HttpUrl.parse(thingId)!!) {
            if (scenario != null) {
                this.scenario = scenario
            }

            thing = it.component1()

            it.failure { viewModel?.showError(it.message) }

            onComplete?.invoke(this)
        }
    }

    private fun getLayoutIdFor(thing: Thing?): Int? {
        if (layoutId != 0) return layoutId

        return thing?.let {
            onEventFor(GetLayoutEvent(thing = it, scenario = scenario))
        }
    }

    init {
        val typedArray = attrs?.let { context.theme.obtainStyledAttributes(it, R.styleable.ThingScreenlet, 0, 0) }

        layoutId = typedArray?.getResourceId(R.styleable.ThingScreenlet_layoutId, 0) ?: 0

        val scenarioId = typedArray?.getString(R.styleable.ThingScreenlet_scenario) ?: ""

        scenario = Scenario.stringToScenario?.invoke(scenarioId) ?: when (scenarioId.toLowerCase()) {
            "detail", "" -> Detail
            "row" -> Row
            else -> Custom(scenarioId)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> onEventFor(event: Event<T>): T? = when (event) {
        is ClickEvent -> screenletEvents?.onClickEvent(layout as BaseView, event.view, event.thing) as? T
        is GetLayoutEvent -> {
            (screenletEvents?.onGetCustomLayout(this, event.view, event.thing, event.scenario) ?:
                layoutIds[event.thing.type[0]]?.get(event.scenario)) as? T
        }
    }
}

interface ViewModel {
    var thing: Thing?
    fun showError(message: String?)
}