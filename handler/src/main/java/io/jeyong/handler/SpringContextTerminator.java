package io.jeyong.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public final class SpringContextTerminator extends ApplicationTerminator {

    private final ApplicationContext applicationContext;
    private final int exitCode;

    public SpringContextTerminator(final ApplicationContext applicationContext, final int exitCode,
                                   final String terminationMessagePath, final String terminationMessage) {
        super(terminationMessagePath, terminationMessage);
        this.applicationContext = applicationContext;
        this.exitCode = exitCode;
    }

    @Override
    protected int getExitCode() {
        return SpringApplication.exit(applicationContext, () -> exitCode);
    }
}
