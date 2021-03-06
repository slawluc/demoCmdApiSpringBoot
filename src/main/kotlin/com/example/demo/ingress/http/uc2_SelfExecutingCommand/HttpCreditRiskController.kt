package com.example.demo.ingress.http.uc2_SelfExecutingCommand

import com.example.demo.UseCase2
import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.CreditRiskResponse
import com.example.demo.business.assessCreditRisk.uc2_SelfExecutingCommand.AssessCreditRiskCmd
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import com.example.demo.ingress.http.HttpRiskCreditCheckAdapter
import com.example.demo.ingress.http.RestCreditCheck
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody


@UseCase2
@Controller("/")
class HttpCreditRiskController(
        private val assessCreditRiskCmd: AssessCreditRiskCmd,
        private val httpRiskCreditCheckAdapter: HttpRiskCreditCheckAdapter
) {

    @PostMapping("risk")
    @ResponseBody
    fun riskCheck(request: ServerHttpRequest,
                  @RequestBody restCreditCheck: RestCreditCheck): Map<String, String> {

        val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter(
                callingContext = FailFastWebFluxHttpCallingContext(request),
                restCreditCheck = restCreditCheck
        )

        val response: Outcome<CreditRiskResponse> = assessCreditRiskCmd(creditRiskRequest)

        return when (response) {
            is Outcome.Result<CreditRiskResponse> -> mapOf( "riskRating" to response.result.riskRating.name )
            is Outcome.Error -> throw RuntimeException(response.reason) //TEMP, YOU WOULDN'T JUST THROW 500 ERRORS!
        }
    }

}

