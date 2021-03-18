package io.holunda.history

import org.camunda.bpm.engine.impl.context.Context
import org.camunda.bpm.engine.impl.history.handler.DbHistoryEventHandler
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Transactional
open class AsyncHistoryEventListener(
        private val dbHistoryEventHandler: DbHistoryEventHandler,
        private val processEngineConfiguration: SpringProcessEngineConfiguration
){

    companion object {
        val LOGGER : Logger = getLogger(AsyncHistoryEventListener::class.java)
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async("asyncHistoryEventExecutor")
    open fun handleEventAsync(event: AsyncHistoryEvent) {
        try {
            LOGGER.debug("Handling history event: $event")
            Context.setCommandContext(processEngineConfiguration.commandContextFactory.createCommandContext())
            event.historyEvents.forEach(dbHistoryEventHandler::handleEvent)
            Context.getCommandContext().dbEntityManager.flush()
        } finally {
            Context.removeCommandContext()
        }
    }

}
