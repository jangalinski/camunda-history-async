package io.holunda.history

import org.camunda.bpm.engine.impl.history.event.HistoryEvent
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

class AsyncHistoryEventHandler(
        private val publisher: ApplicationEventPublisher
) : HistoryEventHandler {

    private val registered: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }
    private val historyEvents: ThreadLocal<MutableList<HistoryEvent>> = ThreadLocal.withInitial { mutableListOf() }

    override fun handleEvent(historyEvent: HistoryEvent) {
        historyEvents.get().add(historyEvent)

        // register synchronization only once
        if (!registered.get()) {
            // send the result

            TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {

                override fun afterCommit() {
                    publisher.publishEvent(AsyncHistoryEvent(historyEvents.get()))
                }

                override fun afterCompletion(status: Int) {
                    historyEvents.remove()
                    registered.remove()
                }

            })

            // mark as registered
            registered.set(true)
        }

    }

    override fun handleEvents(historyEvents: MutableList<HistoryEvent>) {
        historyEvents.forEach { handleEvent(it) }
    }

}

data class AsyncHistoryEvent(
        val historyEvents: List<HistoryEvent>
)
