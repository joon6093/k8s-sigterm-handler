package io.jeyong.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SigtermHandlerConfiguration {

    private static final String SIGNAL_TYPE = "TERM";
    private static final int EXIT_CODE = 0;

    @Bean
    public ApplicationTerminator applicationTerminator(final ApplicationContext applicationContext) {
        return new SpringContextTerminator(applicationContext, EXIT_CODE);
    }

    @Bean
    public SignalHandlerRegistrar sigtermHandlerRegister(final ApplicationTerminator applicationTerminator) {
        return new SignalHandlerRegistrar(applicationTerminator, SIGNAL_TYPE);
    }
}
