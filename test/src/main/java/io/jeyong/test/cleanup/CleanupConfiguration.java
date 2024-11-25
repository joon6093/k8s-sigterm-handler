package io.jeyong.test.cleanup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CleanupConfiguration {

    @Bean
    public PreDestroyHandler preDestroyHandler() {
        return new PreDestroyHandler();
    }

    @Bean
    public ContextClosedEventHandler contextClosedEventHandler() {
        return new ContextClosedEventHandler();
    }

    @Bean
    public ShutdownHookHandler shutdownHookHandler() {
        ShutdownHookHandler shutdownHookHandler = new ShutdownHookHandler();
        shutdownHookHandler.registerShutdownHook();
        return shutdownHookHandler;
    }
}
