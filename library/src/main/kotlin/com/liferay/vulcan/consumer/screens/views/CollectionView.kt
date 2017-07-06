package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.adapter.ThingAdapter
import com.liferay.vulcan.consumer.screens.adapter.ThingViewHolder


open class CollectionView(context: Context, attrs: AttributeSet) : BaseView(context, attrs) {

    val recyclerView by lazy {
        findViewById(R.id.collection_recycler_view) as RecyclerView
    }

    var customLayout: Pair<Int, (View, CollectionView) -> ThingViewHolder>? = null

    override var thing: Thing? by converter<Collection> {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ThingAdapter(R.layout.thing_default, it, this@CollectionView)
    }
}