package com.example.demo.framework.command.coroutine

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Component
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@Component
class CoroutineCommandExecutorService {

    suspend operator fun <C: CommandRequest, R> invoke(request: C, command: Command<C, R>): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        //change to run on some other thread
        val timedOutcome: TimedValue<Outcome<R>> = measureTimedValue {
            command(request)
        }
        //kotlin.runCatching {  }.onFailure {  }

        //TODO catch some exceptions maybe
        //TODO do some things after
        println("This took : ${timedOutcome.duration} milliseconds")

        return timedOutcome.value
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    operator fun <C: CommandRequest, R> invoke(request: C, flowCommand: FlowCommand<C, R>): Flow<Outcome<R>> {
        return flowCommand(request)
                .onStart { println("Do (log, measure, etc) something because the cmd flow is commencing") }
                .onEach { outcome: Outcome<R> -> println("Received outcome in my cmd flow: $outcome") }
                .onCompletion { println("Doing something on the cmd flow completion") }
    }

}

