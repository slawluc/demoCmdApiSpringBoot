@file:Suppress("DuplicatedCode")

package com.example.demo.business.assessCreditRisk.uc6_Coroutine

import com.example.demo.UseCase6
import com.example.demo.business.assessCreditRisk.*
import com.example.demo.framework.command.coroutine.Command
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.coroutine.CoroutinePortExecutorService
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import com.example.demo.port.assessMediumLoanCreditRisk.uc6_Coroutine.MediumRiskRequestPort
import org.springframework.stereotype.Component

/**
 * No difference to the command implementation in UseCase1.  Duplicated for clarity only.
 */

@UseCase6
@Component
class AssessCreditRiskCmd(
        private val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        private val mediumRiskRequestPort: MediumRiskRequestPort,
        private val coroutinePortExecutorService: CoroutinePortExecutorService
): Command<CreditRiskRequest, CreditRiskResponse> {

    override suspend operator fun invoke(request: CreditRiskRequest): Outcome<CreditRiskResponse> {

        val riskRatingOutcome = when (request.loanAmount.amount) {
            in 0..1000 -> assessSmallSizedLoanRisk(request)
            in 1001..10_000 -> assessMedSizedLoanRisk(request)
            in 10_001..100_000 -> assessLargeLoanRisk(request)
            else -> Outcome.Error("No big whale lending please!")
        }

        return riskRatingOutcome.map { rating -> CreditRiskResponse(request.customerId, rating) }
    }


    private suspend fun assessSmallSizedLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            smallLoanOrchestrator.assessRisk(request.customerId, request.loanAmount)


    private suspend fun assessMedSizedLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> {
        val portRequest = CreditRiskMediumRequest(
                customerId = request.customerId,
                loanAmount = request.loanAmount,
                callingContext = request.callingContext
        )

        val response: Outcome<CreditMediumResponse> = coroutinePortExecutorService(portRequest, mediumRiskRequestPort)
        return response.map(CreditMediumResponse::riskRating)
    }


    private suspend fun assessLargeLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            Outcome.Result(RiskRating.JUNK)
}


