package io.jeyong.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class SigtermHandlerProperties {

    private boolean enabled = true;

    private int exitCode = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(final int exitCode) {
        this.exitCode = exitCode;
    }
}
