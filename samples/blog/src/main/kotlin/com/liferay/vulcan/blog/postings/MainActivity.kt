package com.liferay.vulcan.blog.postings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
import com.liferay.vulcan.consumer.screens.ScreenletEvents
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.CollectionView
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
            (it.viewModel as CollectionView).customLayout = Pair(R.layout.blog_posting_row,
                { layout, thingView ->
                    object : CollectionView.ThingAdapter.ThingViewHolder(layout, thingView) {
                        val headline by lazy { layout.findViewById(R.id.headline) as TextView }
                        val creator by lazy { layout.findViewById(R.id.creator) as TextView }

                        override var thing: Thing? = null
                            set(value) {
                                super.thing = value

                                value?.let {
                                    headline.text = it["headline"] as String
                                    creator.text = (it["creator"] as Relation).id
                                }
                            }
                    }
                })
        }
        thingScreenlet.screenletEvents = this

    }

    override fun <T : BaseView> onClickEvent(baseView: T, view: View, thing: Thing) = View.OnClickListener {
        startActivity<DetailActivity>("id" to thing.id)
    }

}