package io.jeyong.handler;

import jakarta.annotation.PostConstruct;
import sun.misc.Signal;

public final class SignalHandlerRegistrar {

    private final ApplicationTerminator applicationTerminator;
    private final String signalType;

    public SignalHandlerRegistrar(final ApplicationTerminator applicationTerminator, final String signalType) {
        this.applicationTerminator = applicationTerminator;
        this.signalType = signalType;
    }

    @PostConstruct
    public void registerHandler() {
        Signal.handle(new Signal(signalType), applicationTerminator.handleTermination());
    }
}
