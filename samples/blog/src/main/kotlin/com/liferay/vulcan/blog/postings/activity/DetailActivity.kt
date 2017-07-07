package com.liferay.vulcan.blog.postings.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.liferay.vulcan.blog.postings.R
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Relation
import com.liferay.vulcan.consumer.model.get
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import okhttp3.HttpUrl
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

class DetailActivity : AppCompatActivity() {

    val thingScreenlet by lazy { findViewById(R.id.thing_screenlet) as ThingScreenlet }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

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
                            val authorId = (thing["creator"] as Relation).id
                            startActivity<AuthorActivity>("id" to authorId)
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