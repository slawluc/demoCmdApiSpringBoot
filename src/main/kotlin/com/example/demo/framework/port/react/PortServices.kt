package com.example.demo.framework.port.react


import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReactivePortExecutorService {

    operator fun <R: PortRequest, V> invoke(request: R, port: MonoToMonoPort<R, V>): Mono<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return invoke(Mono.just(request), port)
    }

    operator fun <R: PortRequest, V> invoke(request: Mono<R>, port: MonoToMonoPort<R, V>): Mono<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return port.invoke(request)
    }

    operator fun <R: PortRequest, V> invoke(request: R, port: MonoToFluxPort<R, V>): Flux<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return invoke(Mono.just(request), port)
    }

    operator fun <R: PortRequest, V> invoke(request: Mono<R>, port: MonoToFluxPort<R, V>): Flux<Outcome<V>> {
        //TODO wrap with metrics, logs, etc
        return port.invoke(request)
    }

    operator fun <R: PortRequest, V> invoke(request: Flux<R>, port: FluxToFluxPort<R, V>): Flux<Outcome<V>> {
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