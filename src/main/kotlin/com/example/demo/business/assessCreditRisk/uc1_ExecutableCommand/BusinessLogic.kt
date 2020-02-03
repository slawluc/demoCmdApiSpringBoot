@file:Suppress("DuplicatedCode")

package com.example.demo.business.assessCreditRisk.uc1_ExecutableCommand

import com.example.demo.UseCase1
import com.example.demo.business.assessCreditRisk.*
import com.example.demo.framework.command.blocking.Command
import com.example.demo.framework.common.Outcome
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import com.example.demo.port.assessMediumLoanCreditRisk.uc2_SelfExecutingPort.MediumRiskRequestPort
import org.springframework.stereotype.Component

@UseCase1
@Component
class AssessCreditRiskCmd(
        val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        val mediumRiskRequestPort: MediumRiskRequestPort,
        val authCheckService: AuthCheckService
): Command<CreditRiskRequest, CreditRiskResponse> {

    override fun invoke(request: CreditRiskRequest): Outcome<CreditRiskResponse> {

        val authorised = authCheckService.authorised(request)
        if (authorised.error) {
            return authorised as Outcome<CreditRiskResponse>
        }

        //TODO bug for negative money :)
        val riskRatingOutcome = when (request.loanAmount.amount) {
            in 0..1000 -> assessSmallSizedLoanRisk(request)
            in 1001..10_000 -> assessMedSizedLoanRisk(request)
            in 10_001..100_000 -> assessLargeLoanRisk(request)
            else -> Outcome.Error("No big whale lending please!")
        }

        return riskRatingOutcome.map { rating -> CreditRiskResponse(request.customerId, rating) }
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


