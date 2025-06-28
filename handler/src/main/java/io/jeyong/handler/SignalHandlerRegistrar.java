package io.jeyong.handler;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import sun.misc.Signal;

public final class SignalHandlerRegistrar implements ApplicationRunner {

    private final ApplicationTerminator applicationTerminator;
    private final String signalType;

    public SignalHandlerRegistrar(final ApplicationTerminator applicationTerminator, final String signalType) {
        this.applicationTerminator = applicationTerminator;
        this.signalType = signalType;
    }

    @Override
    public void run(final ApplicationArguments args) {
        Signal.handle(new Signal(signalType), applicationTerminator.handleTermination());
    }
}
