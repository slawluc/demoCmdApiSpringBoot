package com.example.demo.ingress.http.uc4_ReactiveMonoResponseStream

import com.example.demo.UseCase4
import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.CreditRiskResponse
import com.example.demo.business.assessCreditRisk.uc4_ReactiveMonoResponseStream.AssessCreditRiskCmd
import com.example.demo.framework.command.react.ReactiveCommandExecutorService
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import com.example.demo.ingress.http.HttpRiskCreditCheckAdapter
import com.example.demo.ingress.http.RestCreditCheck
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Mono


@UseCase4
@Controller("/")
class HttpCreditRiskController(
        private val assessCreditRiskCmd: AssessCreditRiskCmd,
        private val reactiveCommandExecutorService: ReactiveCommandExecutorService,
        private val httpRiskCreditCheckAdapter: HttpRiskCreditCheckAdapter
) {

    @PostMapping("risk")
    @ResponseBody
    fun riskCheck(request: ServerHttpRequest,
                  @RequestBody restCreditCheck: RestCreditCheck): Mono<Map<String, String>> {

        //Either THIS
//        val requestMono = Mono.fromCallable {
//            val callingContext: CallingContext = FailFastWebFluxHttpCallingContext(request)
//            val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter(callingContext, restCreditCheck)
//            creditRiskRequest
//        }
//        val response: Mono<Outcome<CreditRiskResponse>> = reactiveCommandExecutorService(creditRiskRequest, assessCreditRiskCmd)

        //OR THIS DEPENDING UPON HOW EXPENSIVE YOU CONVERSION AND ERROR HANDLING IS!!
        val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter(
                callingContext = FailFastWebFluxHttpCallingContext(request),
                restCreditCheck = restCreditCheck
        )

        val response: Mono<Outcome<CreditRiskResponse>> = reactiveCommandExecutorService(creditRiskRequest, assessCreditRiskCmd)

        return response.map { outcome: Outcome<CreditRiskResponse> ->
            when (outcome) {

                is Outcome.Result<CreditRiskResponse> -> mapOf(
                        "riskRating" to outcome.result.riskRating.name
                )

                is Outcome.Error -> mapOf(
                        "riskRating" to "NA",
                        "reason" to outcome.reason
                )

            }
        }
    }

}

