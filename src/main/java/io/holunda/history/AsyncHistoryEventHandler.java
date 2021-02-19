package io.holunda.history;


import org.camunda.bpm.engine.context.ProcessEngineContext;
import org.camunda.bpm.engine.impl.context.ProcessEngineContextImpl;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.concurrent.Callable;


public class AsyncHistoryEventHandler implements HistoryEventHandler {

    private final ApplicationEventPublisher publisher;
    private final DbHistoryEventHandler dbHistoryEventHandler;

    public AsyncHistoryEventHandler(ApplicationEventPublisher publisher, DbHistoryEventHandler dbHistoryEventHandler) {
        this.publisher = publisher;
        this.dbHistoryEventHandler = dbHistoryEventHandler;
    }

    @Override
    public void handleEvent(HistoryEvent historyEvent) {
        publisher.publishEvent(new AsyncHistoryEvent(historyEvent));
    }

    @Override
    public void handleEvents(List<HistoryEvent> historyEvents) {
        historyEvents.forEach(this::handleEvent);
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEventAsync(AsyncHistoryEvent event) throws Exception {

        ProcessEngineContext.withNewProcessEngineContext((Callable<Void>) () -> {
            dbHistoryEventHandler.handleEvent(event.getHistoryEvent());
            return null;
        });

    }
}
