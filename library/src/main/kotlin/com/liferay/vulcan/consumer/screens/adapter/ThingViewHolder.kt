package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.observeNonNull
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.Row

open class ThingViewHolder(itemView: View, listener: Listener) : RecyclerView.ViewHolder(itemView) {

    val thingScreenlet by bindNonNull<ThingScreenlet>(R.id.thing_screenlet)

    open var thing: Thing? by observeNonNull {
        itemView.setOnClickListener { view ->
            listener.onClickedRow(view, it)?.onClick(itemView)
        }

        thingScreenlet.scenario = Row
        thingScreenlet.thing = thing
    }

    interface Listener {
        fun onClickedRow(view: View, thing: Thing): View.OnClickListener?
    }
}