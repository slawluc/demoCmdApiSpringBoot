package com.example.demo.framework.command.coroutine

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome
import kotlinx.coroutines.flow.Flow

interface Command<C: CommandRequest, R> {
    suspend operator fun invoke(request: C): Outcome<R>
}

interface FlowCommand<C: CommandRequest, R> {
    operator fun invoke(request: C): Flow<Outcome<R>>
}