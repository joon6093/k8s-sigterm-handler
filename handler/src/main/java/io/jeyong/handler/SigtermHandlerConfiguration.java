package io.jeyong.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SigtermHandlerConfiguration {

    @Bean
    public ApplicationTerminator applicationTerminator() {
        return new SystemTerminator();
    }

    @Bean
    public SignalHandlerRegistrar sigtermHandlerRegister(final ApplicationTerminator applicationTerminator) {
        return new SignalHandlerRegistrar(applicationTerminator);
    }
}
