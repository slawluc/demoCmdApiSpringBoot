@file:Suppress("DuplicatedCode")

package com.example.demo.business.assessCreditRisk.uc5_ReactiveFluxResponseStream

import com.example.demo.UseCase5
import com.example.demo.business.assessCreditRisk.*
import com.example.demo.framework.command.react.MonoToFluxCmd
import com.example.demo.framework.common.Outcome
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import com.example.demo.port.assessMediumLoanCreditRisk.uc2_SelfExecutingPort.MediumRiskRequestPort
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

/**
 * No difference to the command implementation in UseCase1.  Duplicated for clarity only.
 */

@UseCase5
@Component
class AssessCreditRiskCmd(
        val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        val mediumRiskRequestPort: MediumRiskRequestPort
): MonoToFluxCmd<CreditRiskRequest, CreditRiskResponse> {

    override fun invoke(request: Mono<CreditRiskRequest>): Flux<Outcome<CreditRiskResponse>> =
         Flux.from(request)
                .repeat(20) //Contrived example to stream results from command
                .delayElements(Duration.ofSeconds(1))
                .flatMap { creditRiskRequest -> assessRisk(creditRiskRequest) }

    private fun assessRisk(request: CreditRiskRequest): Mono<Outcome<CreditRiskResponse>> {
        val ratingOutcomeMono = when (request.loanAmount.amount) {
            in 0..1000 -> assessSmallSizedLoanRisk(request)
            in 1001..10_000 -> assessMedSizedLoanRisk(request)
            in 10_001..100_000 -> assessLargeLoanRisk(request)
            else -> Mono.just(Outcome.Error("No big whale lending please!"))
        }
        return ratingOutcomeMono.map { ratingOutcome ->
            ratingOutcome.map { rating ->
                CreditRiskResponse(request.customerId, rating)
            }
        }
    }

    private fun assessSmallSizedLoanRisk(request: CreditRiskRequest): Mono<Outcome<RiskRating>> =
            Mono.just(smallLoanOrchestrator.assessRisk(request.customerId, request.loanAmount))


    private fun assessMedSizedLoanRisk(request: CreditRiskRequest): Mono<Outcome<RiskRating>> {
        val portRequest = CreditRiskMediumRequest(
                customerId = request.customerId,
                loanAmount = request.loanAmount,
                callingContext = request.callingContext
        )

        //TODO get a Mono back from the port stack
        val response: Outcome<CreditMediumResponse> = mediumRiskRequestPort.execute(portRequest)

        return Mono.just(response.map(CreditMediumResponse::riskRating))
    }


    private fun assessLargeLoanRisk(request: CreditRiskRequest): Mono<Outcome<RiskRating>> =
            Mono.just(Outcome.Result(RiskRating.JUNK))
}


