package com.example.demo.framework.common

sealed class Outcome<R>(val error: Boolean) {

    fun <T> map(block: (R) -> T) : Outcome<T> =
            when (this) {
                is Result -> Result(block(result))
                is Error -> this as Outcome<T>
            }

    class Result<R>(val result: R): Outcome<R>(false)

    class Error<R>(val reason: String = "no reason specified"): Outcome<R>(true) //maybe a reason
}


