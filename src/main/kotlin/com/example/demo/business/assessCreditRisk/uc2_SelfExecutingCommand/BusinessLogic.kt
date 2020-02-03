@file:Suppress("DuplicatedCode")

package com.example.demo.business.assessCreditRisk.uc2_SelfExecutingCommand

import com.example.demo.UseCase2
import com.example.demo.business.assessCreditRisk.*
import com.example.demo.framework.command.blocking.CommandExecutorService
import com.example.demo.framework.command.blocking.uc2_SelfExecutingCommand.SelfExecutingCommand
import com.example.demo.framework.common.Outcome
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import com.example.demo.port.assessMediumLoanCreditRisk.uc2_SelfExecutingPort.MediumRiskRequestPort
import org.springframework.stereotype.Component

@UseCase2
@Component
class AssessCreditRiskCmd(
        val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        val mediumRiskRequestPort: MediumRiskRequestPort,
        commandExecutorService: CommandExecutorService
): SelfExecutingCommand<CreditRiskRequest, CreditRiskResponse>(commandExecutorService) {

    override fun onInvocation(request: CreditRiskRequest): Outcome<CreditRiskResponse> {

        val riskRatingOutcome = when (request.loanAmount.amount) {
            in 0..1000 -> assessSmallSizedLoanRisk(request)
            in 1001..10_000 -> assessMedSizedLoanRisk(request)
            in 10_001..100_000 -> assessLargeLoanRisk(request)
            else -> Outcome.Error("No big whale lending please!")
        }

        return riskRatingOutcome.map { riskRating -> CreditRiskResponse(request.customerId, riskRating) }
    }

    private fun assessSmallSizedLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            smallLoanOrchestrator.assessRisk(request.customerId, request.loanAmount)

    private fun assessMedSizedLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> {
        val portRequest = CreditRiskMediumRequest(
                customerId = request.customerId,
                loanAmount = request.loanAmount,
                callingContext = request.callingContext
        )

        val response: Outcome<CreditMediumResponse> = mediumRiskRequestPort.execute(portRequest)

        return response.map(CreditMediumResponse::riskRating)
    }

    private fun assessLargeLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            Outcome.Result(RiskRating.JUNK)
}


