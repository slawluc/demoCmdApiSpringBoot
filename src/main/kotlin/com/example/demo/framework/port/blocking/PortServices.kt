package com.example.demo.framework.port.blocking

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest
import org.springframework.stereotype.Component
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@Component
class PortExecutorService {

    operator fun <C: PortRequest, R> invoke(request: C, port: Port<C, R>): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        val timedOutcome: TimedValue<Outcome<R>> = measureTimedValue {
            port(request)
        }

        //TODO catch some exceptions maybe
        //TODO do some things after
        println("This took : ${timedOutcome.duration} milliseconds")

        return timedOutcome.value
    }
}
