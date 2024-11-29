package io.jeyong.handler;

import sun.misc.SignalHandler;

public abstract class ApplicationTerminator {

    private final String terminationMessagePath;
    private final String terminationMessage;

    protected ApplicationTerminator(final String terminationMessagePath, final String terminationMessage) {
        this.terminationMessagePath = terminationMessagePath;
        this.terminationMessage = terminationMessage;
    }

    public SignalHandler handleTermination() {
        return signal -> {
            FileUtils.writeToFile(terminationMessagePath, terminationMessage);
            System.exit(getExitCode());
        };
    }

    protected abstract int getExitCode();
}
