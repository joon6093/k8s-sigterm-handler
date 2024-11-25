package io.jeyong.test.cleanup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutdownHookHandler {

    public static final String SHUTDOWN_HOOK_LOG = "Executing JVM Shutdown Hook...";

    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info(SHUTDOWN_HOOK_LOG)));
    }
}
