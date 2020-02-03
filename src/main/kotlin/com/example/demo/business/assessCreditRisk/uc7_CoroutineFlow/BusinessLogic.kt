@file:Suppress("DuplicatedCode")

package com.example.demo.business.assessCreditRisk.uc7_CoroutineFlow

import com.example.demo.UseCase7
import com.example.demo.business.assessCreditRisk.*
import com.example.demo.framework.command.coroutine.FlowCommand
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.coroutine.CoroutinePortExecutorService
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import com.example.demo.port.assessMediumLoanCreditRisk.uc7_CoroutineFlow.MediumRiskRequestPort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Component

/**
 * No difference to the command implementation in UseCase1.  Duplicated for clarity only.
 */

@ExperimentalCoroutinesApi
@UseCase7
@Component
class AssessCreditRiskCmd(
        private val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        private val mediumRiskRequestPort: MediumRiskRequestPort,
        private val coroutinePortExecutorService: CoroutinePortExecutorService
): FlowCommand<CreditRiskRequest, CreditRiskResponse> {

    override operator fun invoke(request: CreditRiskRequest): Flow<Outcome<CreditRiskResponse>> {
        //Contrived repeats and delays
        return flow {
            repeat(20) {
                val riskRatingOutcome = when (request.loanAmount.amount) {
                    in 0..1000 -> emit(assessSmallSizedLoanRisk(request))
                    in 1001..10_000 -> emitAll(assessMedSizedLoanRisk(request))
                    in 10_001..100_000 -> emit(assessLargeLoanRisk(request))
                    else -> emit(Outcome.Error("No big whale lending please!"))
                }
                delay(1_000L)
            }
        }.map { ratingOutcome: Outcome<RiskRating> ->
            ratingOutcome.map { rating: RiskRating ->
                CreditRiskResponse(request.customerId, rating)
            }
        }
    }


    private suspend fun assessSmallSizedLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            smallLoanOrchestrator.assessRisk(request.customerId, request.loanAmount)


    private suspend fun assessMedSizedLoanRisk(request: CreditRiskRequest): Flow<Outcome<RiskRating>> {
        val portRequest = CreditRiskMediumRequest(
                customerId = request.customerId,
                loanAmount = request.loanAmount,
                callingContext = request.callingContext
        )

        val responseFlow = coroutinePortExecutorService(portRequest, mediumRiskRequestPort)

        return responseFlow
                .map { responseOutcome: Outcome<CreditMediumResponse> ->
                    responseOutcome.map { creditMediumResponse ->
                        creditMediumResponse.riskRating
                    }
                }
    }


    private suspend fun assessLargeLoanRisk(request: CreditRiskRequest): Outcome<RiskRating> =
            Outcome.Result(RiskRating.JUNK)
}


