package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ClickAction
import com.liferay.vulcan.consumer.screens.views.CollectionView

open class ThingViewHolder(itemView: View, val collectionView: CollectionView) : RecyclerView.ViewHolder(itemView) {

    val thingType by lazy {
        itemView.findViewById(R.id.thing_type) as? TextView
    }

    open var thing: Thing? = null
        set(value) {
            field = value

            value?.let {
                itemView?.setOnClickListener {
                    val onClickListener = collectionView.sendAction(ClickAction(itemView, value))

                    onClickListener?.onClick(itemView)
                }

                thingType?.text = value.type.joinToString()
            }
        }
}