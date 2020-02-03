package com.example.demo.business.assessCreditRisk.uc1_ExecutableCommand

import com.example.demo.UseCase1
import com.example.demo.framework.command.blocking.CommandExecutorService
import com.example.demo.framework.command.blocking.uc1_ExecutableCommand.ExecutableCommand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Because we're not using a SelfExecutingCommand, we either have to wire in both the command and the commandService
 */
@UseCase1
@Configuration
class Config {
    @Bean
    fun assessCreditRiskExecCmd(commandExecutorService: CommandExecutorService, assessCreditRiskCmd: AssessCreditRiskCmd) =
            ExecutableCommand(commandExecutorService, assessCreditRiskCmd)
}
