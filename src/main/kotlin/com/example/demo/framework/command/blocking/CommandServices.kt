package com.example.demo.framework.command.blocking

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome
import org.springframework.stereotype.Component
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue


@Component
class CommandExecutorService {

    operator fun <C: CommandRequest, R> invoke(request: C, command: Command<C, R>): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        val timedOutcome: TimedValue<Outcome<R>> = measureTimedValue {
            command(request)
        }

        //TODO catch some exceptions maybe
        //TODO do some things after
        println("This took : ${timedOutcome.duration} milliseconds")

        return timedOutcome.value
    }
}
