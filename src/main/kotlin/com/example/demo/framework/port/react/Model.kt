package com.example.demo.framework.port.react

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MonoToMonoPort<R: PortRequest, V>: MonoRequestStreamPort<R, Mono<Outcome<V>>>

interface MonoToFluxPort<R: PortRequest, V>: MonoRequestStreamPort<R, Flux<Outcome<V>>>

interface FluxToFluxPort<R: PortRequest, V>: FluxRequestStreamPort<R, Flux<Outcome<V>>>

interface MonoRequestStreamPort<R: PortRequest, V> {
    fun invoke(request: Mono<R>): V
}

interface FluxRequestStreamPort<R: PortRequest, V> {
    fun invoke(request: Flux<R>): V
}