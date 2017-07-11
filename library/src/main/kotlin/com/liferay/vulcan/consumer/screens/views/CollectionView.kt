package com.liferay.vulcan.consumer.screens.views

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
import com.liferay.vulcan.consumer.screens.ClickEvent
import com.liferay.vulcan.consumer.screens.GetLayoutEvent
import com.liferay.vulcan.consumer.screens.Scenario
import com.liferay.vulcan.consumer.screens.ViewInfo
import com.liferay.vulcan.consumer.screens.adapter.ThingAdapter

open class CollectionView(context: Context, attrs: AttributeSet) : BaseView(context, attrs), ThingAdapter.Listener {
    val recyclerView by bindNonNull<RecyclerView>(R.id.collection_recycler_view)

    override var thing: Thing? by converter<Collection> {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ThingAdapter(R.layout.thing_default, it, this)
    }

    override fun onGetLayout(thing: Thing): ViewInfo? =
        sendEvent(GetLayoutEvent(this, thing, Scenario.ROW))

    override fun onClickedRow(view: View, thing: Thing): OnClickListener? =
        sendEvent(ClickEvent(view, thing))
}