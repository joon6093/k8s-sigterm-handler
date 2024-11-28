package io.jeyong.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        prefix = "kubernetes.handler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(SigtermHandlerProperties.class)
public class SigtermHandlerConfiguration {

    private static final String SIGNAL_TYPE = "TERM";

    private final SigtermHandlerProperties sigtermHandlerProperties;

    public SigtermHandlerConfiguration(final SigtermHandlerProperties sigtermHandlerProperties) {
        this.sigtermHandlerProperties = sigtermHandlerProperties;
    }

    @Bean
    public ApplicationTerminator applicationTerminator(final ApplicationContext applicationContext) {
        return new SpringContextTerminator(applicationContext, sigtermHandlerProperties.getExitCode());
    }

    @Bean
    public SignalHandlerRegistrar sigtermHandlerRegister(final ApplicationTerminator applicationTerminator) {
        return new SignalHandlerRegistrar(applicationTerminator, SIGNAL_TYPE);
    }
}
