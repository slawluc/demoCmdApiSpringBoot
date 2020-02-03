package com.example.demo.port.assessMediumLoanCreditRisk.uc7_CoroutineFlow

import com.example.demo.UseCase7
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.coroutine.FlowPort
import com.example.demo.port.assessMediumLoanCreditRisk.CreditMediumResponse
import com.example.demo.port.assessMediumLoanCreditRisk.CreditRiskMediumRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Component

@UseCase7
@Component
class MediumRiskRequestPort : FlowPort<CreditRiskMediumRequest, CreditMediumResponse> {
    override operator fun invoke(request: CreditRiskMediumRequest): Flow<Outcome<CreditMediumResponse>> {
        return flow {
            println("UseCase7: Coroutine: MediumRiskRequestPort")
            emit(Outcome.Result(CreditMediumResponse(RiskRating.A)))
            emit(Outcome.Result(CreditMediumResponse(RiskRating.B)))
        }
    }
}

