package io.jeyong.handler;

import jakarta.annotation.PostConstruct;
import sun.misc.Signal;

public class SignalHandlerRegistrar {

    public static final String SIGNAL_TYPE = "TERM";
    public static final int EXIT_CODE = 0;

    private final ApplicationTerminator applicationTerminator;

    public SignalHandlerRegistrar(final ApplicationTerminator applicationTerminator) {
        this.applicationTerminator = applicationTerminator;
    }

    @PostConstruct
    public void registerHandler() {
        Signal.handle(new Signal(SIGNAL_TYPE), applicationTerminator.handleTermination(EXIT_CODE));
    }
}
