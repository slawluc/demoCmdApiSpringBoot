package com.example.demo.ingress.http

import com.example.demo.business.CreditRiskRequest
import com.example.demo.business.CreditRiskRequestCommand
import com.example.demo.business.CreditRiskResponse
import com.example.demo.business.MonetaryValue
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

data class RestCreditCheck(
        val customerId: String,
        val amount: Long,
        val currency: String
)

@Controller("/")
class CreditRiskController(
        private val creditRiskRequestCommand: CreditRiskRequestCommand,
        private val httpRiskCreditCheckAdapter: HttpRiskCreditCheckAdapter
) {

    @PostMapping("risk")
    @ResponseBody
    suspend fun riskCheck(request: ServerHttpRequest,
                          @RequestBody restCreditCheck: RestCreditCheck): Map<String, String> {
        val callingContext: CallingContext = FailFastWebFluxHttpCallingContext(request)
        val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter.adapt(callingContext, restCreditCheck)
        val response: Outcome<CreditRiskResponse> = creditRiskRequestCommand.execute(creditRiskRequest)

        return when (response) {
            is Outcome.Result<CreditRiskResponse> -> mapOf( "riskRating" to response.result.riskRating.name )
            is Outcome.Error -> throw RuntimeException(response.reason) //TEMP, YOU WOULDN'T JUST THROW 500 ERRORS!
        }
    }

}

@Component
class HttpRiskCreditCheckAdapter {
    fun adapt(callingContext: CallingContext, restCreditCheck: RestCreditCheck): CreditRiskRequest {
        return CreditRiskRequest(
                ctx = callingContext,
                customerId = CustomerIdentificationNumber(restCreditCheck.customerId),
                loanAmount = MonetaryValue(restCreditCheck.amount, restCreditCheck.currency)
        )
    }
}
