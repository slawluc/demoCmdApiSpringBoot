package com.example.demo.framework.port

import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.Outcome


abstract class PortRequest(val callingContext: CallingContext)

abstract class BasePort<C: PortRequest, R> {
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