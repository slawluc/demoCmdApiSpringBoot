package com.example.demo.port.assessMediumLoanCreditRisk.uc6_Coroutine

import com.example.demo.UseCase6
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.coroutine.Port
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import org.springframework.stereotype.Component

@UseCase6
@Component
class MediumRiskRequestPort : Port<CreditRiskMediumRequest, CreditMediumResponse> {
    override suspend operator fun invoke(request: CreditRiskMediumRequest): Outcome<CreditMediumResponse> {
        println("UseCase6: Coroutine: MediumRiskRequestPort")
        return Outcome.Result(CreditMediumResponse(RiskRating.B))
    }
}

