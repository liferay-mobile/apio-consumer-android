package com.liferay.vulcan.consumer.screens.views.detail

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.bindNonNull
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.ClickEvent
import com.liferay.vulcan.consumer.screens.adapter.ThingAdapter
import com.liferay.vulcan.consumer.screens.views.BaseView

open class CollectionDetailView(context: Context, attrs: AttributeSet) : BaseView(context, attrs), ThingAdapter.Listener {
    val recyclerView by bindNonNull<RecyclerView>(R.id.collection_recycler_view)

    override var thing: Thing? by converter<Collection> {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ThingAdapter(it, this)
    }

    override fun onClickedRow(view: View, thing: Thing): OnClickListener? =
        sendEvent(ClickEvent(view, thing))
}