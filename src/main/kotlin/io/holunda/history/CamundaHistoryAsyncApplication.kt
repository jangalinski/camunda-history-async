package io.holunda.history

import org.camunda.bpm.application.ProcessApplication
import org.camunda.bpm.engine.ProcessEngineConfiguration
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@SpringBootApplication
@ProcessApplication
@EnableAsync
open class CamundaHistoryAsyncApplication {

    @Bean
    open fun asyncCamundaHistoryConfiguration(publisher: ApplicationEventPublisher) = AsyncCamundaHistoryConfiguration(publisher)

    @Bean
    open fun asyncHistoryEventListener(configuration: ProcessEngineConfiguration) = AsyncHistoryEventListener(
            DbHistoryEventHandler(), configuration as SpringProcessEngineConfiguration)

    @Bean
    open fun disableTelemetry() = object : AbstractCamundaConfiguration() {

            override fun preInit(configuration: ProcessEngineConfigurationImpl) {
                configuration.isInitializeTelemetry = false
                configuration.isTelemetryReporterActivate = false
            }

            override fun toString() = "disableTelemetry"

        }

    @Bean(name = ["asyncHistoryEventExecutor"])
    open fun asyncExecutor() = ThreadPoolTaskExecutor().apply {
        this.maxPoolSize = 100
        this.threadNamePrefix = "HistoryEvent-"
        this.initialize()
    }

}

fun main(args: Array<String>) {
    runApplication<CamundaHistoryAsyncApplication>(*args)
}
