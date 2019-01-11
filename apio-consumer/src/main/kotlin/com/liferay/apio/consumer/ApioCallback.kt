package com.liferay.apio.consumer

import android.os.Build
import android.support.annotation.RequiresApi
import com.github.kittinunf.result.Result
import com.liferay.apio.consumer.model.Thing
import java.util.function.Consumer

/**
 * @author Paulo Cruz
 */
@RequiresApi(Build.VERSION_CODES.N)
class ApioCallback(private val onSuccess: Consumer<Thing>, private val onFailure: Consumer<Exception>)
	: Function1<Result<Thing, Exception>, Unit> {

	override fun invoke(result: Result<Thing, Exception>) =
		result.fold({
			onSuccess.accept(it)
		}, {
			onFailure.accept(it)
		})

}