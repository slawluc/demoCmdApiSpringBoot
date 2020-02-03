package com.example.demo.port.assessMediumLoanCreditRisk.uc2_SelfExecutingPort

import com.example.demo.*
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.blocking.uc2_SelfExecutingPort.SelfExecutingPort
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import org.springframework.stereotype.Component


@UseCase1
@UseCase2
@UseCase3
@UseCase5
@Component
class MediumRiskRequestPort : SelfExecutingPort<CreditRiskMediumRequest, CreditMediumResponse>() {
    override fun performExecute(portRequest: CreditRiskMediumRequest): Outcome<CreditMediumResponse> =
        Outcome.Result(CreditMediumResponse(RiskRating.B))
}

