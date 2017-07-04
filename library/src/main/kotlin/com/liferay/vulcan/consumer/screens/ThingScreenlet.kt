package com.liferay.vulcan.consumer.screens

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.ThingView
import okhttp3.HttpUrl

open class BaseScreenlet(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs) {

    var layout: View? = null
}

class ThingScreenlet(context: Context, attrs: AttributeSet) :
    BaseScreenlet(context, attrs) {

    var screenletEvents: ScreenletEvents? = null

    val layoutIds: Map<String, Int> = mapOf(
        "BlogPosting" to R.layout.blog_posting_default,
        "Collection" to R.layout.collection_default
    )

    val reference: Int

    var thing: Thing? = null
        set(value) {
            field = value
            viewModel?.thing = value
        }

    val viewModel: ViewModel? get() = layout as ViewModel

    fun load(thingId: String, onComplete: ((ThingScreenlet) -> Unit)? = null) {
        fetch(HttpUrl.parse(thingId)!!) {
            val layoutId = if (reference == 0)
                it.component1()?.type?.get(0)
                    ?.let { layoutIds[it] }
                    ?: R.layout.thing_default
            else reference

            layout = this.inflate(layoutId)

            addView(layout)

            (layout as? ThingView)?.screenlet = this

            it.fold(
                success = { thing = it },
                failure = { viewModel?.showError(it.message) }
            )
            onComplete?.invoke(this)
        }
    }

    init {
        val typedArray =
            context.theme.obtainStyledAttributes(
                attrs, R.styleable.ThingScreenlet, 0, 0)

        reference =
            typedArray.getResourceId(R.styleable.ThingScreenlet_layoutId, 0)
    }

    fun <T> onEventFor(action: Action<T>) = when (action) {
        is ClickAction -> screenletEvents?.onClickEvent(
            layout as ThingView, action.view, action.thing)
    }
}

interface ViewModel {
    var thing: Thing?
    fun showError(message: String?)
}

interface ScreenletEvents {
    fun <T : ThingView> onClickEvent(
        thingView: T, view: View, thing: Thing): OnClickListener?
}