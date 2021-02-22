package io.holunda.history;

import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaHistoryConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.context.ApplicationEventPublisher;

public class AsyncCamundaHistoryConfiguration extends AbstractCamundaConfiguration implements CamundaHistoryConfiguration {

    private final ApplicationEventPublisher publisher;

    public AsyncCamundaHistoryConfiguration(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void preInit(SpringProcessEngineConfiguration configuration) {
        if (camundaBpmProperties.getHistoryLevel() != null) {
            configuration.setHistory(camundaBpmProperties.getHistoryLevel());
        }
        configuration.setHistoryEventHandler(new AsyncHistoryEventHandler(publisher, new DbHistoryEventHandler()));
    }
}
