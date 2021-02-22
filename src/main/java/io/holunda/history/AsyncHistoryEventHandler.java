package io.holunda.history;


import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AsyncHistoryEventHandler implements HistoryEventHandler {

    private final ApplicationEventPublisher publisher;

    public AsyncHistoryEventHandler(ApplicationEventPublisher publisher, DbHistoryEventHandler dbHistoryEventHandler) {
        this.publisher = publisher;
    }

    @Override
    public void handleEvent(HistoryEvent historyEvent) {
        publisher.publishEvent(new AsyncHistoryEvent(historyEvent));
    }

    @Override
    public void handleEvents(List<HistoryEvent> historyEvents) {
        historyEvents.forEach(this::handleEvent);
    }

}
