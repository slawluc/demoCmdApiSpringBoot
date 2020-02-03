package com.example.demo.framework.command.react

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.Outcome
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MonoToMonoCmd<R: CommandRequest, V>: MonoRequestStreamCmd<R, Mono<Outcome<V>>>

interface MonoToFluxCmd<R: CommandRequest, V>: MonoRequestStreamCmd<R, Flux<Outcome<V>>>

interface FluxToFluxCmd<R: CommandRequest, V>: FluxRequestStreamCmd<R, Flux<Outcome<V>>>

interface MonoRequestStreamCmd<R: CommandRequest, V> {
    fun invoke(request: Mono<R>): V
}

interface FluxRequestStreamCmd<R: CommandRequest, V> {
    fun invoke(request: Flux<R>): V
}