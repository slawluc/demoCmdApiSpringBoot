package com.example.demo.ingress.http.uc3_CommandExecutorService

import com.example.demo.UseCase3
import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.CreditRiskResponse
import com.example.demo.business.assessCreditRisk.uc3_CommandExecutorService.AssessCreditRiskCmd
import com.example.demo.framework.command.blocking.CommandExecutorService
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import com.example.demo.ingress.http.HttpRiskCreditCheckAdapter
import com.example.demo.ingress.http.RestCreditCheck
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody


@UseCase3
@Controller("/")
class HttpCreditRiskController(
        private val assessCreditRiskCmd: AssessCreditRiskCmd,
        private val commandExecutorService: CommandExecutorService,
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

        val response: Outcome<CreditRiskResponse> = commandExecutorService(creditRiskRequest, assessCreditRiskCmd)

        return when (response) {
            is Outcome.Result<CreditRiskResponse> -> mapOf( "riskRating" to response.result.riskRating.name )
            is Outcome.Error -> throw RuntimeException(response.reason) //TEMP, YOU WOULDN'T JUST THROW 500 ERRORS!
        }
    }

}

