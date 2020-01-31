package com.example.demo.port.crm

import com.example.demo.business.MonetaryValue
import com.example.demo.business.RiskRating
import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.CustomerIdentificationNumber
import com.example.demo.framework.common.Outcome
import com.example.demo.framework.port.BasePort
import com.example.demo.framework.port.PortRequest
import org.springframework.stereotype.Component

class CreditRiskMediumRequest(
        val customerId: CustomerIdentificationNumber,
        val loanAmount: MonetaryValue,
        callingContext: CallingContext) :  PortRequest(callingContext)


class CreditMediumResponse(val riskRating: RiskRating)


@Component
class MediumRiskRequestPort : BasePort<CreditRiskMediumRequest, CreditMediumResponse>() {
    override fun performExecute(portRequest: CreditRiskMediumRequest): Outcome<CreditMediumResponse> {
        return Outcome.Result(CreditMediumResponse(RiskRating.B))
    }
}

