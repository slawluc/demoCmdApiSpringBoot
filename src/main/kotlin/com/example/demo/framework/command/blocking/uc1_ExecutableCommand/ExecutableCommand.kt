package com.example.demo.framework.command.blocking.uc1_ExecutableCommand

import com.example.demo.framework.command.blocking.Command
import com.example.demo.framework.command.blocking.CommandExecutorService
import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome

class ExecutableCommand<C: CommandRequest, R>(
        private val commandExecutorService: CommandExecutorService,
        private val command: Command<C, R>): Command<C, R> {

    override fun invoke(request:C): Outcome<R> = commandExecutorService(request, command)
}