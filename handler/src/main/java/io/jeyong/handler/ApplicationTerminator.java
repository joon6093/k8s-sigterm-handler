package io.jeyong.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.SignalHandler;

public abstract class ApplicationTerminator {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTerminator.class);

    private final String terminationMessagePath;
    private final String terminationMessage;

    protected ApplicationTerminator(final String terminationMessagePath, final String terminationMessage) {
        this.terminationMessagePath = terminationMessagePath;
        this.terminationMessage = terminationMessage;
    }

    public SignalHandler handleTermination() {
        return signal -> {
            logTerminationSignal(signal.getName());
            FileUtils.writeToFile(terminationMessagePath, terminationMessage);
            System.exit(getExitCode());
        };
    }

    private void logTerminationSignal(final String signalName) {
        logger.info("Received {} signal. Initiating termination handler.", signalName);
    }

    protected abstract int getExitCode();
}
