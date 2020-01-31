package com.example.demo.framework.business

import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.Outcome

abstract class CommandRequest(val callingContext: CallingContext)

abstract class BaseCommand<C: CommandRequest, R> {
    protected abstract fun performExecute(commandRequest: C): Outcome<R>

    @Suppress("UnnecessaryVariable")
    fun execute(commandRequest:C): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        val response: Outcome<R> = performExecute(commandRequest)

        //TODO catch some exceptions maybe
        //TODO do some things after

        return response
    }
}