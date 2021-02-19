package io.holunda.history;


import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Transactional
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
    public void handleEventAsync(AsyncHistoryEvent event) {
        dbHistoryEventHandler.handleEvent(event.getHistoryEvent());
    }
}
