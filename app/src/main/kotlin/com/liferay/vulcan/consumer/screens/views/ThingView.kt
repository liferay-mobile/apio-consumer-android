package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.Action
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.ViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast

open class ThingView(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs), ViewModel, AnkoLogger {

    var screenlet: ThingScreenlet? = null
    val thingId: View? by lazy { findViewById(R.id.thing_id) }
    val thingType: View? by lazy { findViewById(R.id.thing_type) }
    val thingName: View? by lazy { findViewById(R.id.thing_name) }

    fun <T> sendAction(action: Action<T>) = screenlet?.onEventFor(action)

    override var thing: Thing? = null
        set(value) {
            field = value

            value?.apply {
                (thingId as? TextView)?.text = id
                (thingType as? TextView)?.text = type.joinToString()
                (thingName as? TextView)?.text = name
            }

        }

    override fun showError(message: String?) {
        context.longToast("Error loading the thing")
        error { "Error loading the thing " + message }
    }

}