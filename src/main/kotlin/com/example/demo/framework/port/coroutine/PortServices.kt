package com.example.demo.framework.port.coroutine

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Component
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

@Component
class CoroutinePortExecutorService {

    suspend operator fun <C: PortRequest, R> invoke(request: C, port: Port<C, R>): Outcome<R> {

        //TODO do some things before (metrics, logging, etc)

        //change to run on some other thread
        val timedOutcome: TimedValue<Outcome<R>> = measureTimedValue {
            port(request)
        }
        //kotlin.runCatching {  }.onFailure {  }

        //TODO catch some exceptions maybe
        //TODO do some things after
        println("This took : ${timedOutcome.duration} milliseconds")

        return timedOutcome.value
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    operator fun <C: PortRequest, R> invoke(request: C, flowPort: FlowPort<C, R>): Flow<Outcome<R>> {
        return flowPort(request)
                .onStart { println("Do (log, measure, etc) something because the port flow is commencing") }
                .onEach { outcome: Outcome<R> -> println("Received outcome in my port flow: $outcome") }
                .onCompletion { println("Doing something on the port flow completion") }
    }

}