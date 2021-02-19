package io.holunda.history;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;

@SpringBootApplication
@ProcessApplication
public class CamundaHistoryAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaHistoryAsyncApplication.class, args);
    }

  //  @Bean
    public DbHistoryEventHandler dbHistoryEventHandler(@Lazy ProcessEngineConfigurationImpl configuration) {
        var setContext = new Command<Void>() {

            @Override
            public Void execute(CommandContext commandContext) {
                if (Context.getCommandContext() == null) {
                    Context.setCommandContext(commandContext);
                }
                return null;
            }
        };

        return new DbHistoryEventHandler() {
            @Override
            protected DbEntityManager getDbEntityManager() {
                configuration.getCommandExecutorTxRequired().execute(setContext);

                Assert.notNull(Context.getCommandContext(), "context is null");

                return super.getDbEntityManager();
            }
        };
    }

    @Bean
    public AsyncHistoryEventHandler asyncHistoryEventHandler(ApplicationEventPublisher publisher) {
        return new AsyncHistoryEventHandler(publisher, new DbHistoryEventHandler());
    }

    @Bean
    public AsyncCamundaHistoryConfiguration asyncCamundaHistoryConfiguration(AsyncHistoryEventHandler asyncHistoryEventHandler) {
        return new AsyncCamundaHistoryConfiguration(asyncHistoryEventHandler);
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
