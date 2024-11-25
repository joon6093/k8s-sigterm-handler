package io.jeyong.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringContextTerminator extends ApplicationTerminator {

    private final ApplicationContext applicationContext;
    private final int exitCode;

    public SpringContextTerminator(final ApplicationContext applicationContext, final int exitCode) {
        this.applicationContext = applicationContext;
        this.exitCode = exitCode;
    }

