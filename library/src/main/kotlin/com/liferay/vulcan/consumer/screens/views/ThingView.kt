package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.observeNonNull
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.Action
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.ViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast

open class ThingView(context: Context, attrs: AttributeSet) :
    BaseView(context, attrs) {

    val thingId: View? by lazy { findViewById(R.id.thing_id) }
    val thingType: View? by lazy { findViewById(R.id.thing_type) }
    val thingName: View? by lazy { findViewById(R.id.thing_name) }

    override var thing: Thing? by observeNonNull {
        (thingId as? TextView)?.text = it.id
        (thingType as? TextView)?.text = it.type.joinToString()
        (thingName as? TextView)?.text = it.name
    }

}