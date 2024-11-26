package io.jeyong.test.cleanup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
public class ContextClosedEventHandler {

    public static final String CONTEXT_CLOSED_EVENT_LOG = "Handling ContextClosedEvent...";

    @EventListener(ContextClosedEvent.class)
    public void handleContextClosed() {
        log.info(CONTEXT_CLOSED_EVENT_LOG);
    }
}
