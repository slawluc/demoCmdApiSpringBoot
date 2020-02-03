package com.example.demo.port.assessMediumLoanCreditRisk.uc4_ReactiveMonoResponseStream

import com.example.demo.UseCase4
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.react.MonoToMonoPort
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@UseCase4
@Component
class MediumRiskRequestPort : MonoToMonoPort<CreditRiskMediumRequest, CreditMediumResponse> {
    override operator fun invoke(request: Mono<CreditRiskMediumRequest>): Mono<Outcome<CreditMediumResponse>> {
        return request.map { ignoredRequest: CreditRiskMediumRequest ->
            println("UseCase4: Coroutine: MediumRiskRequestPort")
            Outcome.Result(CreditMediumResponse(RiskRating.B))
        }
    }
}

