package com.example.demo.port.assessMediumLoanCreditRisk

import com.example.demo.business.assessCreditRisk.MonetaryValue
import com.example.demo.business.assessCreditRisk.RiskRating
import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.CustomerIdentificationNumber
import com.example.demo.framework.port.PortRequest

class CreditRiskMediumRequest(
        val customerId: CustomerIdentificationNumber,
        val loanAmount: MonetaryValue,
        callingContext: CallingContext) :  PortRequest(callingContext)

data class CreditMediumResponse(val riskRating: RiskRating)