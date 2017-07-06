package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.observeNonNull
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ClickAction
import com.liferay.vulcan.consumer.screens.views.CollectionView

open class ThingViewHolder(itemView: View, val collectionView: CollectionView) : RecyclerView.ViewHolder(itemView) {

    val thingType by lazy {
        itemView.findViewById(R.id.thing_type) as? TextView
    }

    open var thing: Thing? by observeNonNull {
        itemView.setOnClickListener { view ->
            val onClickListener = collectionView.sendAction(ClickAction(view, it))

            onClickListener?.onClick(itemView)
        }

        thingType?.text = it.type.joinToString()
    }
}