package com.example.demo.business.assessCreditRisk

import com.example.demo.framework.common.CustomerIdentificationNumber
import com.example.demo.framework.common.IdentityToken
import com.example.demo.framework.common.Outcome
import org.springframework.stereotype.Component

/**
 * NOTE
 * Put in 'uc1' package, but the following could easily be a shared service
 */
@Component
class AuthCheckService {
    fun authorised(request: CreditRiskRequest) : Outcome<Unit> {
        val identityToken: IdentityToken = request.callingContext.identityToken
        val customerId: CustomerIdentificationNumber = identityToken.customerId

        return when (customerId == request.customerId) {
            true -> Outcome.Result(Unit)
            false -> Outcome.Error("Not authorised to check on another customer")
        }
    }
}

/**
 * This orchestrator ultimately is linked up to port to call an external service..... hardcoding for now
 */
@Component
class SmallLoanCreditRiskServiceCheckOrchestrator {
    fun assessRisk(customerId: CustomerIdentificationNumber, loanAmount: MonetaryValue): Outcome<RiskRating> =
            Outcome.Result(RiskRating.A)
}