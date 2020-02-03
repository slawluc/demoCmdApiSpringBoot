package com.example.demo.ingress.http.uc5_ReactiveFluxResponseStream

import com.example.demo.UseCase5
import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.CreditRiskResponse
import com.example.demo.business.assessCreditRisk.uc5_ReactiveFluxResponseStream.AssessCreditRiskCmd
import com.example.demo.framework.command.react.ReactiveCommandExecutorService
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import com.example.demo.ingress.http.HttpRiskCreditCheckAdapter
import com.example.demo.ingress.http.RestCreditCheck
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import reactor.core.publisher.Flux


@Suppress("DuplicatedCode")
@UseCase5
@Controller("/")
class HttpCreditRiskController(
        private val assessCreditRiskCmd: AssessCreditRiskCmd,
        private val reactiveCommandExecutorService: ReactiveCommandExecutorService,
        private val httpRiskCreditCheckAdapter: HttpRiskCreditCheckAdapter
) {

    @PostMapping("risk", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    fun riskCheck(request: ServerHttpRequest,
                  @RequestBody restCreditCheck: RestCreditCheck): Flux<Map<String, String>> {

        val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter(
                callingContext = FailFastWebFluxHttpCallingContext(request),
                restCreditCheck = restCreditCheck
        )

        val response: Flux<Outcome<CreditRiskResponse>> = reactiveCommandExecutorService(creditRiskRequest, assessCreditRiskCmd)

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

