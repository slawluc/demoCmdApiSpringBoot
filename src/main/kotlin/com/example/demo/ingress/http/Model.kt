package com.example.demo.ingress.http

data class RestCreditCheck(
        val customerId: String,
        val amount: Long,
        val currency: String
)