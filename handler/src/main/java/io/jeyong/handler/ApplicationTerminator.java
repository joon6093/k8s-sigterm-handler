package io.jeyong.handler;

import sun.misc.SignalHandler;

public interface ApplicationTerminator {

    SignalHandler handleTermination(final int status);
}
