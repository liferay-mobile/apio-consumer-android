package com.liferay.vulcan.blog.postings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.github.kittinunf.result.success
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.model.get
import com.liferay.vulcan.consumer.screens.ScreenletEvents
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import com.liferay.vulcan.consumer.screens.views.CollectionView
import com.liferay.vulcan.consumer.screens.views.ThingView
import okhttp3.HttpUrl
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), ScreenletEvents {

    val thingScreenlet by lazy {
        findViewById(R.id.thing_collection) as ThingScreenlet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = "http://192.168.50.222:8080/o/api/group/20149/p/blogs"

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

    override fun <T : ThingView> onClickEvent(
        thingView: T, view: View, thing: Thing) = View.OnClickListener {

        startActivity<SecondActivity>("id" to thing.id)
    }
}

class SecondActivity : AppCompatActivity() {

    val thingScreenlet by lazy {
        findViewById(R.id.thing_screenlet) as ThingScreenlet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val id = intent.getStringExtra("id")

        thingScreenlet.load(id)

        //TODO move to screenlet
        val knowMore = findViewById(R.id.know_more)
        val httpURL = HttpUrl.parse(id)
        fetch(httpURL!!, embedded = listOf("creator")) {
            it.fold(
                success = { thing ->
                    (findViewById(R.id.headline) as TextView).apply {
                        text = thing["headline"] as String
                    }

                    (findViewById(R.id.creator) as TextView).apply {
                        val author = thing.attributes["author"] as Relation
                        text = author.id

                        knowMore.setOnClickListener {
                            fetch(HttpUrl.parse(author.id)!!) {
                                it.success {
                                    text = it["name"] as String
                                }
                            }
                        }
                    }
                },
                failure = {
                    longToast(it.message!!)
                }
            )

        }
    }
}