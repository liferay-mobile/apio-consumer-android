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
import com.liferay.vulcan.consumer.delegates.convert
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ClickAction
import okhttp3.HttpUrl

open class CollectionView(context: Context, attrs: AttributeSet)
    : BaseView(context, attrs) {

    val recyclerView by lazy {
        findViewById(R.id.collection_recycler_view) as RecyclerView
    }

    var customLayout: Pair<Int, (View, CollectionView) -> ThingAdapter.ThingViewHolder>? = null

    override var thing: Thing? by converter<Collection> {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ThingAdapter(
            R.layout.thing_default, it, this@CollectionView)
    }

    class ThingAdapter(val layoutId: Int, collection: Collection, val collectionView: CollectionView) :
        Adapter<ThingAdapter.ThingViewHolder>() {

        val totalItems = collection.totalItems
        val members = collection.members?.toMutableList() ?: mutableListOf()

        //TODO How do we want to model this?
        val nextPage = collection.pages?.next

        override fun onBindViewHolder(holder: ThingViewHolder?, position: Int) {
            //TODO architect add index? per page? page?
            if (members.size > position) {
                holder?.thing = members[position]
            } else {
                nextPage.let {
                    fetch(HttpUrl.parse(nextPage)!!) {
                        it.fold(
                            success = {
                                convert<Collection>(it)?.let {
                                    val moreMembers = it.members
                                    merge(members, moreMembers)
                                    notifyDataSetChanged()
                                }
                            },
                            failure = {}
                        )
                    }
                }
            }
        }

        private fun merge(members: MutableList<Thing>, moreMembers: List<Thing>?) {
            moreMembers?.apply { members.addAll(this) }
        }

        override fun getItemCount(): Int = totalItems ?: 0

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThingViewHolder? {
            return collectionView.customLayout?.let { (customLayoutId, viewHolderCreator) ->
                parent?.inflate(customLayoutId)
                    ?.let { viewHolderCreator(it, collectionView) }
            } ?: parent?.inflate(layoutId)?.let { ThingViewHolder(it, collectionView) }
        }

        open class ThingViewHolder(itemView: View, val collectionView: CollectionView) :
            RecyclerView.ViewHolder(itemView) {

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
    }
}