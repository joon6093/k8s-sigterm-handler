package io.jeyong.handler;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        prefix = "kubernetes.sigterm-handler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(SigtermHandlerProperties.class)
public class SigtermHandlerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SigtermHandlerConfiguration.class);
    private static final String SIGNAL_TYPE = "TERM";

    private final SigtermHandlerProperties sigtermHandlerProperties;

    public SigtermHandlerConfiguration(final SigtermHandlerProperties sigtermHandlerProperties) {
        this.sigtermHandlerProperties = sigtermHandlerProperties;
    }

    @PostConstruct
    public void logInitialization() {
        if (Strings.isBlank(sigtermHandlerProperties.getTerminationMessagePath())) {
            logger.info("Sigterm handler initialized with exitCode: {}", sigtermHandlerProperties.getExitCode());
        } else {
            logger.info(
                    "Sigterm handler initialized with exitCode: {}, terminationMessagePath: {}, terminationMessage: '{}'",
                    sigtermHandlerProperties.getExitCode(),
                    sigtermHandlerProperties.getTerminationMessagePath(),
                    sigtermHandlerProperties.getTerminationMessage());
        }
    }

    @Bean
    public ApplicationTerminator applicationTerminator(final ApplicationContext applicationContext) {
        return new SpringContextTerminator(applicationContext, sigtermHandlerProperties.getExitCode(),
                sigtermHandlerProperties.getTerminationMessagePath(), sigtermHandlerProperties.getTerminationMessage());
    }

    @Bean
    public SignalHandlerRegistrar sigtermHandlerRegister(final ApplicationTerminator applicationTerminator) {
        return new SignalHandlerRegistrar(applicationTerminator, SIGNAL_TYPE);
    }
}
