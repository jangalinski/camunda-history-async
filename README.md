# camunda-history-async
Async decoupling of Camunda history events


## Idea

* register a HistoryEventHandler that just forwards HistoryEvents via ApplicationEventPublisher.
* implement a TransactionalEventListener that forwards the receided events to the DbHistoryEventHandler after the original transaction commits.

_not working so far_!

## Reproduce

* start the spring application
* open the h2-console in the browser (http://localhost:8080/h2-console ,  url: jdbc:h2:mem:camunda-history-async, user&pw:sa)
* query act_hi_actinst .... empty
* open cockpit (http://localhost:8080/, user&pw:admin)
* start process manually in tasklist
* check act_hi_actinst in h2 again ... 

see <https://forum.camunda.org/t/async-history-event-handler-can-it-work/25589>
