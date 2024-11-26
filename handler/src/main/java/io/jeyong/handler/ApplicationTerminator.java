package io.jeyong.handler;

import sun.misc.SignalHandler;

public abstract class ApplicationTerminator {

    public SignalHandler handleTermination() {
        return signal -> System.exit(getExitCode());
    }

    protected abstract int getExitCode();
}
