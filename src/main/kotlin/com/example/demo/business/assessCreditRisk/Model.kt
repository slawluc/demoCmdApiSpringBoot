package com.example.demo.business.assessCreditRisk

import com.example.demo.framework.command.CommandRequest
import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.CustomerIdentificationNumber
import com.example.demo.framework.common.Outcome

data class MonetaryValue(
        val amount: Long,
        val currency: String
)

class CreditRiskRequest(
        val customerId: CustomerIdentificationNumber,
        val loanAmount: MonetaryValue,
        ctx: CallingContext): CommandRequest(ctx) {
}

enum class RiskRating {
        A, B, JUNK
}

data class CreditRiskResponse(
        val customerId: CustomerIdentificationNumber,
        val riskRating: RiskRating
)

fun CreditRiskResponse.outcome(): Outcome.Result<CreditRiskResponse> =
        Outcome.Result(this)
