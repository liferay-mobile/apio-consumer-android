package com.liferay.vulcan.consumer.screens.views

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.graph
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
import com.liferay.vulcan.consumer.screens.ClickAction

open class CollectionView(context: Context, attrs: AttributeSet)
    : ThingView(context, attrs) {

    val recyclerView by lazy {
        findViewById(R.id.collection_recycler_view) as RecyclerView
    }

    var customLayout: Pair<Int, (view: View, thingView: ThingView) -> ThingAdapter.ThingViewHolder>? = null

    override var thing: Thing? = null
        set(value) {
            field = value

            value?.apply {
                val members = (this["members"] as List<Relation>).map {
                    graph[it.id]?.value
                }.filterNotNull()

                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = ThingAdapter(
                    R.layout.thing_default, members, this@CollectionView)
            }

        }

    class ThingAdapter(
        val layoutId: Int, val members: List<Thing>, val thingView: CollectionView) :
        Adapter<ThingAdapter.ThingViewHolder>() {

        override fun onBindViewHolder(holder: ThingViewHolder?, position: Int) {
            holder?.thing = members[position]
        }

        override fun getItemCount(): Int = members.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThingViewHolder? {
            return thingView.customLayout?.let { (customLayoutId, viewHolderCreator) ->
                parent?.inflate(customLayoutId)
                    ?.let {
                        viewHolderCreator(it, thingView)
                    }
            } ?: parent?.inflate(layoutId)?.let { ThingViewHolder(it, thingView) }
        }

        open class ThingViewHolder(itemView: View, val thingView: ThingView) :
            RecyclerView.ViewHolder(itemView) {

            val thingType by lazy {
                itemView.findViewById(R.id.thing_type) as? TextView
            }

            open var thing: Thing? = null
                set(value) {
                    field = value

                    value?.let {
                        itemView?.setOnClickListener {
                            val onClickListener =
                                thingView.sendAction(
                                    ClickAction(itemView, value))

                            onClickListener?.onClick(itemView)
                        }

                        thingType?.text = value.type.joinToString()
                    }
                }
        }

    }
}