package com.example.demo.business

import com.example.demo.framework.business.BaseCommand
import com.example.demo.framework.business.CommandRequest
import com.example.demo.framework.common.CallingContext
import com.example.demo.framework.common.CustomerIdentificationNumber
import com.example.demo.framework.common.IdentityToken
import com.example.demo.framework.common.Outcome
import com.example.demo.port.crm.CreditMediumResponse
import com.example.demo.port.crm.CreditRiskMediumRequest
import com.example.demo.port.crm.MediumRiskRequestPort
import org.springframework.stereotype.Component


data class MonetaryValue(val amount: Long, val currency: String)

class CreditRiskRequest(
        val customerId: CustomerIdentificationNumber,
        val loanAmount: MonetaryValue,
        ctx: CallingContext): CommandRequest(ctx) {
}

enum class RiskRating { A,B,JUNK }

class CreditRiskResponse(val customerId: CustomerIdentificationNumber, val riskRating: RiskRating) {
    fun outcome(): Outcome.Result<CreditRiskResponse> = Outcome.Result(this)
}



@Component
class CreditRiskRequestCommand(
        val smallLoanOrchestrator: SmallLoanCreditRiskServiceCheckOrchestrator,
        val midSizedLoanOrchestrator: MidSizedLoansCreditRiskServiceCheckOrchestrator,
        val mediumRiskRequestPort: MediumRiskRequestPort,
        val authCheckService: AuthCheckService
): BaseCommand<CreditRiskRequest, CreditRiskResponse>() {

    override fun performExecute(request: CreditRiskRequest): Outcome<CreditRiskResponse> {

        val authorised = authCheckService.authorised(request)
        if (authorised.error) {
            return authorised as Outcome<CreditRiskResponse>
        }

        //TODO bug for negative money :)
        val riskRating: Outcome<RiskRating> = when (request.loanAmount.amount) {
            in 0..1000 -> smallLoanOrchestrator.assessRisk(request.customerId, request.loanAmount)
            in 1001..10_000 -> {
                val portRequest = CreditRiskMediumRequest(
                        customerId = request.customerId,
                        loanAmount = request.loanAmount,
                        callingContext = request.callingContext
                )
                val response: Outcome<CreditMediumResponse> = mediumRiskRequestPort.execute(portRequest)
                response.map { r -> r.riskRating }
            }
            in 10_001..100_000 -> Outcome.Result(RiskRating.JUNK)
            else -> Outcome.Error("No big lending please!")
        }

        return riskRating.map { rating -> CreditRiskResponse(request.customerId, rating) }
    }
}




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

@Component
class SmallLoanCreditRiskServiceCheckOrchestrator {
    fun assessRisk(customerId: CustomerIdentificationNumber, loanAmount: MonetaryValue): Outcome<RiskRating> {
        return Outcome.Result(RiskRating.A)
    }
}

@Component
class MidSizedLoansCreditRiskServiceCheckOrchestrator {
    fun assessMidSizedRisk(customerId: CustomerIdentificationNumber, loanAmount: MonetaryValue): Outcome<RiskRating> {
        return Outcome.Result(RiskRating.B)
    }
}