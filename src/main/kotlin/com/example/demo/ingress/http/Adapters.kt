package com.example.demo.ingress.http

import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.MonetaryValue
import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.CustomerIdentificationNumber
import org.springframework.stereotype.Component

@Component
class HttpRiskCreditCheckAdapter {
    operator fun invoke(callingContext: CallingContext, restCreditCheck: RestCreditCheck): CreditRiskRequest {
        return CreditRiskRequest(
                ctx = callingContext,
                customerId = CustomerIdentificationNumber(restCreditCheck.customerId),
                loanAmount = MonetaryValue(restCreditCheck.amount, restCreditCheck.currency)
        )
    }
}
