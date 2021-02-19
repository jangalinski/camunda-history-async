package io.holunda.history;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaHistoryConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;

public class AsyncCamundaHistoryConfiguration extends AbstractCamundaConfiguration implements CamundaHistoryConfiguration {

    private final AsyncHistoryEventHandler asyncHistoryEventHandler;

    public AsyncCamundaHistoryConfiguration(AsyncHistoryEventHandler asyncHistoryEventHandler) {
        this.asyncHistoryEventHandler = asyncHistoryEventHandler;
    }

    @Override
    public void preInit(SpringProcessEngineConfiguration configuration) {
        configuration.setHistoryEventHandler(asyncHistoryEventHandler);
        if (camundaBpmProperties.getHistoryLevel() != null) {
            configuration.setHistory(camundaBpmProperties.getHistoryLevel());
        }
    }
}
