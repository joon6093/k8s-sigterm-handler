package io.jeyong.handler;

import sun.misc.SignalHandler;

public class SystemTerminator implements ApplicationTerminator {

    @Override
    public SignalHandler handleTermination(final int status) {
        return signal -> System.exit(status);
    }
}
