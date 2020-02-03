package com.example.demo.framework.port.blocking.uc2_SelfExecutingPort

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest


abstract class SelfExecutingPort<C: PortRequest, R> {
    protected abstract fun performExecute(portRequest: C): Outcome<R>

    @Suppress("UnnecessaryVariable")
    fun execute(portRequest:C): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        val response: Outcome<R> = performExecute(portRequest)

        //TODO catch some exceptions maybe
        //TODO do some things after

        return response
    }
}