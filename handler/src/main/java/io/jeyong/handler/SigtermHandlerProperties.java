package io.jeyong.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;

// @formatter:off
/**
 * Configuration properties for Kubernetes SIGTERM handling.
 *
 * <p>
 * Allows customization of the Sigterm Handler's behavior, including whether it is enabled,
 * the exit code to use during graceful termination, and optional termination message settings.
 * </p>
 *
 * <ul>
 *     <li><b>kubernetes.sigterm-handler.enabled:</b> Set whether the handler is enabled or disabled. (default: true)</li>
 *     <li><b>kubernetes.sigterm-handler.exit-code:</b> Set the exit code for graceful application termination. (default: 0)</li>
 *     <li><b>kubernetes.sigterm-handler.termination-message-path:</b> Set the file path where the termination message should be written. (default: not set)</li>
 *     <li><b>kubernetes.sigterm-handler.termination-message:</b> Set the content of the termination message written to the specified path. (default: SIGTERM signal received. Application has been terminated successfully.)</li>
 * </ul>
 *
 * <pre>
 * Example configuration (YAML):
 * {@code
 * kubernetes:
 *   sigterm-handler:
 *     enabled: true
 *     exit-code: 0
 *     termination-message-path: /dev/termination-log
 *     termination-message: SIGTERM signal received...
 * }
 * </pre>
 *
 * <pre>
 * Example configuration (Properties):
 * {@code
 * kubernetes.sigterm-handler.enabled=true
 * kubernetes.sigterm-handler.exit-code=0
 * kubernetes.sigterm-handler.termination-message-path=/dev/termination-log
 * kubernetes.sigterm-handler.termination-message=SIGTERM signal received...
 * }
 * </pre>
 *
 * <p>
 * The Sigterm Handler is primarily designed for Kubernetes
 * but can also be utilized in Docker or other environments requiring signal handling functionality.
 * </p>
 *
 * @author jeyong
 * @since 1.2
 * @see SigtermHandlerConfiguration
 */
// @formatter:on
@ConfigurationProperties(prefix = "kubernetes.sigterm-handler")
public final class SigtermHandlerProperties {

    private boolean enabled = true;
    private int exitCode = 0;
    private String terminationMessagePath;
    private String terminationMessage = "SIGTERM signal received. Application has been terminated successfully.";

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

    public String getTerminationMessagePath() {
        return terminationMessagePath;
    }

    public void setTerminationMessagePath(final String terminationMessagePath) {
        this.terminationMessagePath = terminationMessagePath;
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }

    public void setTerminationMessage(final String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }
}
