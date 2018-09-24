package com.liferay.apio.consumer.authenticator

import okhttp3.Request


/**
 * @author Paulo Cruz
 */
interface ApioAuthenticator {

    fun authenticate(request: Request): Request

}