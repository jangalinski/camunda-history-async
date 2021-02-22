package io.holunda.history;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ProcessApplication
public class CamundaHistoryAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaHistoryAsyncApplication.class, args);
    }

    @Bean
    public AsyncCamundaHistoryConfiguration asyncCamundaHistoryConfiguration(ApplicationEventPublisher publisher) {
        return new AsyncCamundaHistoryConfiguration(publisher);
    }

    @Bean
    public AsyncHistoryEventListener asyncHistoryEventListener(ProcessEngineConfiguration configuration) {
        return new AsyncHistoryEventListener(new DbHistoryEventHandler(), ((SpringProcessEngineConfiguration) configuration));
    }

    @Bean
    public AbstractCamundaConfiguration disableTelemetry() {
        return new AbstractCamundaConfiguration() {
            @Override
            public void preInit(SpringProcessEngineConfiguration configuration) {
                configuration.setInitializeTelemetry(false);
                configuration.setTelemetryReporterActivate(false);
            }

            @Override
            public String toString() {
                return "disableTelemetry";
            }
        };
    }

}
