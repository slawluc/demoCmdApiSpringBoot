package com.example.demo.framework.ingress

import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.IdentityToken
import com.example.demo.framework.ingress.BankHttpHeadersKeys.CHANNEL_ID
import com.example.demo.framework.ingress.BankHttpHeadersKeys.IDENTITY_TOKEN
import org.springframework.http.HttpRequest
import org.springframework.http.server.reactive.ServerHttpRequest


/**
 * Fail fast Reactive Calling Context
 * If you want to fail immediately if headers are missing.
 * BETTER!!
 */
class FailFastHttpCallingContext(request: HttpRequest): CallingContext {
    override val channelId: String = request[CHANNEL_ID]
    override val identityToken: IdentityToken = IdentityToken(request[IDENTITY_TOKEN])

    private operator fun HttpRequest.get(key: String): String =
            headers[key]?.first() ?: throw IllegalStateException("No header '$key' set")
}

/**
 * Fail fast Reactive Calling Context
 * If you want to fail immediately if headers are missing.
 * BETTER!!
 */
class FailFastWebFluxHttpCallingContext(request: ServerHttpRequest): CallingContext {
    override val channelId: String = request[CHANNEL_ID]
    override val identityToken: IdentityToken = IdentityToken(request[IDENTITY_TOKEN])

    private operator fun ServerHttpRequest.get(key: String): String =
            this.headers[key]?.first() ?: throw IllegalStateException("No header '$key' set")
}

/**
 * Lazy Reactive Calling Context
 * If you want to initialise and then let the mandatory fields fail when the business logic requests it
 * NOT IDEAL!!!
 */
class LazyReactiveCallingContext(private val request: ServerHttpRequest): CallingContext {
    override val channelId: String get() = request[CHANNEL_ID]
    override val identityToken: IdentityToken get() = IdentityToken(request[IDENTITY_TOKEN])

    private operator fun ServerHttpRequest.get(key: String): String =
            this.headers[key]?.first() ?: throw IllegalStateException("No header '$key' set")
}

typealias BankHeaders = String

object BankHttpHeadersKeys {
    const val CHANNEL_ID: BankHeaders = "x-channel-id"
    const val IDENTITY_TOKEN: BankHeaders = "x-trust-token"
}