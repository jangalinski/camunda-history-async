package io.holunda.history;

import org.camunda.bpm.engine.impl.history.event.HistoryEvent;

import java.io.Serializable;
import java.util.Objects;

public class AsyncHistoryEvent implements Serializable {
    private final HistoryEvent historyEvent;

    public AsyncHistoryEvent(HistoryEvent historyEvent) {
        this.historyEvent = historyEvent;
    }

    public HistoryEvent getHistoryEvent() {
        return historyEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsyncHistoryEvent that = (AsyncHistoryEvent) o;
        return Objects.equals(historyEvent, that.historyEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyEvent);
    }

    @Override
    public String toString() {
        return "AsyncHistoryEvent{" +
                "historyEvent=" + historyEvent +
                '}';
    }
}
