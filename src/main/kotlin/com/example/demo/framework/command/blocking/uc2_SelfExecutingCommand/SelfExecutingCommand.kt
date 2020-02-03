package com.example.demo.framework.command.blocking.uc2_SelfExecutingCommand

import com.example.demo.framework.command.blocking.Command
import com.example.demo.framework.command.blocking.CommandExecutorService
import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome

abstract class SelfExecutingCommand<C: CommandRequest, R>(
        private val commandExecutorService: CommandExecutorService
): Command<C, R> {

    internal abstract fun onInvocation(request: C): Outcome<R>

    override fun invoke(request:C): Outcome<R> =
        commandExecutorService(request, object: Command<C, R> {
            override fun invoke(request: C): Outcome<R> = onInvocation(request)
        })
}