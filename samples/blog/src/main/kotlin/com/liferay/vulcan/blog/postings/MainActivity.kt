package com.liferay.vulcan.blog.postings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.delegates.converter
import com.liferay.vulcan.consumer.model.BlogPosting
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.ScreenletEvents
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.CollectionView
import com.liferay.vulcan.consumer.screens.adapter.ThingViewHolder
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), ScreenletEvents {

    val thingScreenlet by lazy {
        findViewById(R.id.thing_collection) as ThingScreenlet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = "http://192.168.0.156:8080/o/api/group/20143/p/blogs"

        thingScreenlet.load(id) {
            (it.viewModel as CollectionView).customLayout = R.layout.blog_posting_row to ::BlogPostingViewHolder
        }

        thingScreenlet.screenletEvents = this

    }

    override fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing) = View.OnClickListener {
        startActivity<DetailActivity>("id" to thing.id)
    }


    class BlogPostingViewHolder(itemView: View, collectionView: CollectionView) :
        ThingViewHolder(itemView, collectionView) {

            val headline by lazy { itemView.findViewById(R.id.headline) as TextView }
            val creator by lazy { itemView.findViewById(R.id.creator) as TextView }

            override var thing: Thing? by converter<BlogPosting> {
                super.thing = this.thing

                headline.text = it.headline
                creator.text = it.creator?.id
            }
    }

}