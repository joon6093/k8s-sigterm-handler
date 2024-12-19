package io.jeyong.handler;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import sun.misc.Signal;

public final class SignalHandlerRegistrar {

    private final ApplicationTerminator applicationTerminator;
    private final String signalType;

    public SignalHandlerRegistrar(final ApplicationTerminator applicationTerminator, final String signalType) {
        this.applicationTerminator = applicationTerminator;
        this.signalType = signalType;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void registerHandler() {
        Signal.handle(new Signal(signalType), applicationTerminator.handleTermination());
    }
}
