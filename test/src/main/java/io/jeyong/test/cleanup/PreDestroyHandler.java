package io.jeyong.test.cleanup;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreDestroyHandler {

    public static final String PRE_DESTROY_LOG = "Executing @PreDestroy cleanup logic...";

    @PreDestroy
    public void cleanup() {
        log.info(PRE_DESTROY_LOG);
    }
}
