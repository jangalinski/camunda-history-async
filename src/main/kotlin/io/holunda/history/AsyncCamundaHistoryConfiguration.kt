package io.holunda.history

import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.spring.boot.starter.configuration.CamundaHistoryConfiguration
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration
import org.springframework.context.ApplicationEventPublisher

class AsyncCamundaHistoryConfiguration(
        private val publisher: ApplicationEventPublisher
) : CamundaHistoryConfiguration, AbstractCamundaConfiguration() {

    override fun preInit(configuration: ProcessEngineConfigurationImpl) {
        if (camundaBpmProperties.historyLevel != null) {
            configuration.history = camundaBpmProperties.historyLevel
        }
        configuration.historyEventHandler = AsyncHistoryEventHandler(publisher)
    }

    override fun postInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
        super<AbstractCamundaConfiguration>.postInit(processEngineConfiguration)
    }

    override fun postProcessEngineBuild(processEngine: ProcessEngine?) {
        super<AbstractCamundaConfiguration>.postProcessEngineBuild(processEngine)
    }

}
