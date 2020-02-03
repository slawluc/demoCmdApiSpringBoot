package com.example.demo.port.assessMediumLoanCreditRisk.uc5_ReactiveFluxResponseStream

import com.example.demo.UseCase5
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.react.MonoToFluxPort
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@UseCase5
@Component
class MediumRiskRequestPort : MonoToFluxPort<CreditRiskMediumRequest, CreditMediumResponse> {
    override operator fun invoke(request: Mono<CreditRiskMediumRequest>): Flux<Outcome<CreditMediumResponse>> =
        Flux.from(request)
            .repeat(2) //contrived repeat to demonstrate flux capability
            .delayElements(Duration.ofSeconds(1))
            .map { ignoredRequestForNow: CreditRiskMediumRequest ->
                println("UseCase5: MonoToFluxPort: MediumRiskRequestPort")
                Outcome.Result(CreditMediumResponse(RiskRating.B))
            }
}

