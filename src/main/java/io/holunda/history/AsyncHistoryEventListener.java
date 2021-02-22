package io.holunda.history;

import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional
public class AsyncHistoryEventListener {

    private final DbHistoryEventHandler dbHistoryEventHandler;
    private final SpringProcessEngineConfiguration processEngineConfiguration;

    public AsyncHistoryEventListener(DbHistoryEventHandler dbHistoryEventHandler, SpringProcessEngineConfiguration processEngineConfiguration) {
        this.dbHistoryEventHandler = dbHistoryEventHandler;
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEventAsync(AsyncHistoryEvent event) {
        Context.setCommandContext(processEngineConfiguration.getCommandContextFactory().createCommandContext());
        dbHistoryEventHandler.handleEvent(event.getHistoryEvent());
        Context.getCommandContext().getDbEntityManager().flush();
    }

}
