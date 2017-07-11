package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bind
import com.liferay.vulcan.consumer.delegates.observeNonNull
import com.liferay.vulcan.consumer.model.Thing

open class ThingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val thingType by bind<TextView>(R.id.thing_type)

    open var thing: Thing? by observeNonNull {
//        itemView.setOnClickListener { view ->
//            val onClickListener = collectionView.sendEvent(ClickEvent(view, it))
//
//            onClickListener?.onClick(itemView)
//        }

        thingType?.text = it.type.joinToString()
    }
}