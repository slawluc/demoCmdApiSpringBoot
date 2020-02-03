package com.example.demo.framework.command.react

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReactiveCommandExecutorService {

    operator fun <R: CommandRequest, V> invoke(request: R, command: MonoToMonoCmd<R, V>): Mono<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return invoke(Mono.just(request), command)
    }

    operator fun <R: CommandRequest, V> invoke(request: Mono<R>, command: MonoToMonoCmd<R, V>): Mono<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return command.invoke(request)
    }

    operator fun <R: CommandRequest, V> invoke(request: R, command: MonoToFluxCmd<R, V>): Flux<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return invoke(Mono.just(request), command)
    }

    operator fun <R: CommandRequest, V> invoke(request: Mono<R>, command: MonoToFluxCmd<R, V>): Flux<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return command.invoke(request)
    }

    operator fun <R: CommandRequest, V> invoke(request: Flux<R>, command: FluxToFluxCmd<R, V>): Flux<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return Flux.empty()
    }

    private fun someCommonInvocationStack() {
        //TODO
//        val reqMono: Mono<C> = requestMono
//                .log()
//                .doOnNext {
//                    //do something else something
//                }
    }

}

