package io.jeyong.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;

// @formatter:off
/**
 * Configuration properties for Kubernetes SIGTERM handling.
 *
 * <p>
 * Allows customization of the SIGTERM handler's behavior, including whether it is enabled and the exit code to use
 * during graceful termination.
 * </p>
 *
 * <ul>
 *     <li><b>kubernetes.handler.enabled:</b> Set whether the handler is enabled or disabled (default: true).</li>
 *     <li><b>kubernetes.handler.exit-code:</b> Set the exit code for graceful application termination (default: 0).</li>
 * </ul>
 *
 * <pre>
 * Example configuration (YAML):
 * {@code
 * kubernetes:
 *   handler:
 *     enabled: true
 *     exit-code: 1
 * }
 * </pre>
 *
 * <pre>
 * Example configuration (Properties):
 * {@code
 * kubernetes.handler.enabled=true
 * kubernetes.handler.exit-code=1
 * }
 * </pre>
 *
 * <p>
 * By default, the handler is enabled, and the application terminates with an exit code of 0,
 * marking the Kubernetes Pod as "Completed."
 * </p>
 *
 * @author jeyong
 * @since 1.0
 * @see SigtermHandlerConfiguration
 */
// @formatter:on
@ConfigurationProperties(prefix = "kubernetes.handler")
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
