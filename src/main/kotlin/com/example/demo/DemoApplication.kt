package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile

@SpringBootApplication
class DemoApplication

/**
 * SET YOUR ACTIVE PROFILE as an environment property
 * 'UseCase1' or 'UseCase2'
 */
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Retention
@Profile("UseCase1")
annotation class UseCase1

@Retention
@Profile("UseCase2")
annotation class UseCase2

@Retention
@Profile("UseCase3")
annotation class UseCase3

@Retention
@Profile("UseCase4")
annotation class UseCase4

@Retention
@Profile("UseCase5")
annotation class UseCase5

@Retention
@Profile("UseCase6")
annotation class UseCase6

@Retention
@Profile("UseCase7")
annotation class UseCase7