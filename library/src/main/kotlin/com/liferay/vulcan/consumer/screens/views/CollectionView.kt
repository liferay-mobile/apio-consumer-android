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
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.graph
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
import com.liferay.vulcan.consumer.screens.ClickAction
import okhttp3.HttpUrl

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
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = ThingAdapter(
                    R.layout.thing_default, value, this@CollectionView)
            }

        }

    class ThingAdapter(val layoutId: Int, thing: Thing,
        val thingView: CollectionView) : Adapter<ThingAdapter.ThingViewHolder>() {

        val totalItems = (thing["totalItems"] as Double).toInt()
        val members = extractElements(thing)
        //TODO How do we want to model this?
        val previousPage = (thing["view"] as Relation)["previous"] as? String
        val nextPage = (thing["view"] as Relation)["next"] as? String

        override fun onBindViewHolder(holder: ThingViewHolder?, position: Int) {
            //TODO architect add index? per page? page?
            if (members.size > position) {
                holder?.thing = members[position]
            } else {
                nextPage.let {
                    fetch(HttpUrl.parse(nextPage)!!) {
                        it.fold(
                            success = {
                                val moreMembers = extractElements(it)
                                merge(members, moreMembers)
                                notifyDataSetChanged()
                            },
                            failure = {}
                        )
                    }
                }
            }
        }

        private fun merge(members: MutableList<Thing>, moreMembers: MutableList<Thing>) {
            members.addAll(moreMembers)
        }

        fun extractElements(it: Thing) =
            (it["members"] as List<Relation>).map {
                graph[it.id]?.value
            }.filterNotNull().toMutableList()

        override fun getItemCount(): Int = totalItems

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThingViewHolder? {
            return thingView.customLayout?.let { (customLayoutId, viewHolderCreator) ->
                parent?.inflate(customLayoutId)
                    ?.let { viewHolderCreator(it, thingView) }
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
                            val onClickListener = thingView.sendAction(ClickAction(itemView, value))
                            onClickListener?.onClick(itemView)
                        }

                        thingType?.text = value.type.joinToString()
                    }
                }
        }
    }
}