package com.example.demo.ingress.http.uc7_CoroutineFlow

import com.example.demo.UseCase7
import com.example.demo.business.assessCreditRisk.CreditRiskRequest
import com.example.demo.business.assessCreditRisk.CreditRiskResponse
import com.example.demo.business.assessCreditRisk.uc7_CoroutineFlow.AssessCreditRiskCmd
import com.example.demo.framework.command.coroutine.CoroutineCommandExecutorService
import com.example.demo.framework.common.*
import com.example.demo.framework.ingress.FailFastWebFluxHttpCallingContext
import com.example.demo.ingress.http.HttpRiskCreditCheckAdapter
import com.example.demo.ingress.http.RestCreditCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody


@UseCase7
@Controller("/")
class HttpCoroutineCreditRiskController(
        private val assessCreditRiskCmd: AssessCreditRiskCmd,
        private val coroutineCommandExecutorService: CoroutineCommandExecutorService,
        private val httpRiskCreditCheckAdapter: HttpRiskCreditCheckAdapter
) {

    @PostMapping("risk", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    suspend fun riskCheck(request: ServerHttpRequest,
                          @RequestBody restCreditCheck: RestCreditCheck): Flow<Map<String, String>> {

        val creditRiskRequest: CreditRiskRequest = httpRiskCreditCheckAdapter(
                callingContext = FailFastWebFluxHttpCallingContext(request),
                restCreditCheck = restCreditCheck
        )

        val responseFlow: Flow<Outcome<CreditRiskResponse>> = coroutineCommandExecutorService(creditRiskRequest, assessCreditRiskCmd)

        return responseFlow.map { response ->
            when (response) {
                is Outcome.Result<CreditRiskResponse> -> mapOf(
                        "riskRating" to response.result.riskRating.name
                )
                is Outcome.Error -> mapOf(
                        "riskRating" to "NA",
                        "reason" to response.reason

                )
            }
        }
    }

}

