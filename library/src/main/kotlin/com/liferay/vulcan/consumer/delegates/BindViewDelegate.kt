package com.liferay.vulcan.consumer.delegates

import android.app.Activity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewNotFoundException(message: String) : Throwable(message)

fun <V : View> View.bind(id: Int): ReadOnlyProperty<View, V?> = bind(id) { findViewById(it) }

fun <V : View> Activity.bind(id: Int): ReadOnlyProperty<Activity, V?> = bind(id) { findViewById(it) }

fun <V : View> ViewHolder.bind(id: Int): ReadOnlyProperty<ViewHolder, V?> = bind(id) { itemView.findViewById(it) }

fun <V : View> View.bindNonNull(id: Int): ReadOnlyProperty<View, V> = bindNonNull(id) { findViewById(it) }

fun <V : View> Activity.bindNonNull(id: Int): ReadOnlyProperty<Activity, V> = bindNonNull(id) { findViewById(it) }

fun <V : View> ViewHolder.bindNonNull(id: Int): ReadOnlyProperty<ViewHolder, V> = bindNonNull(id) {
    itemView.findViewById(it)
}

private fun <T, V : View> bind(id: Int, viewFinder: T.(Int) -> View?) = Lazy { t: T, _ -> t.viewFinder(id) as? V }

private fun <T, V : View> bindNonNull(id: Int, viewFinder: T.(Int) -> View?) = Lazy { t: T, property ->
    t.viewFinder(id) as V? ?: throw ViewNotFoundException("View with id $id for variable `${property.name}` not found")
}

private class Lazy<in T, out V>(val viewFinder: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private var value: V? = null

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == null) {
            value = viewFinder(thisRef, property)
        }

        return value as V
    }
}