package com.example.demo.framework.port.coroutine

import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.PortRequest
import kotlinx.coroutines.flow.Flow

interface Port<C: PortRequest, R> {
    suspend operator fun invoke(request: C): Outcome<R>
}

interface FlowPort<C: PortRequest, R> {
    operator fun invoke(request: C): Flow<Outcome<R>>
}