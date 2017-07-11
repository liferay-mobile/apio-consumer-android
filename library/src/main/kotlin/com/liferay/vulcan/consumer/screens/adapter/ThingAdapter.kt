package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import com.liferay.vulcan.consumer.delegates.convert
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.Row
import com.liferay.vulcan.consumer.screens.ViewInfo
import okhttp3.HttpUrl

class ThingAdapter(val layoutId: Int, collection: Collection, val listener: Listener) :
    Adapter<ThingViewHolder>(), ThingViewHolder.Listener {

    override fun onClickedRow(view: View, thing: Thing): View.OnClickListener? = listener.onClickedRow(view, thing)

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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun merge(members: MutableList<Thing>, moreMembers: List<Thing>?) {
        moreMembers?.apply { members.addAll(this) }
    }

    override fun getItemCount(): Int = totalItems ?: 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThingViewHolder? {
        return members.getOrNull(viewType)
            ?.let { listener.onGetLayout(it) }
            ?.let { it as? Row }
            ?.let { row ->
                parent?.inflate(row.id)?.let {
                    row.viewHolderCreator.invoke(it, this)
                }
            } ?: parent?.inflate(layoutId)?.let { ThingViewHolder(it, this) }
    }

    interface Listener {
        fun onGetLayout(thing: Thing): ViewInfo?
        fun onClickedRow(view: View, thing: Thing): View.OnClickListener?
    }
}