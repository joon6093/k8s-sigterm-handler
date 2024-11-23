package io.jeyong.test.unit;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static io.jeyong.handler.SignalHandlerRegistrar.EXIT_CODE;
import static org.assertj.core.api.Assertions.assertThat;

import io.jeyong.handler.SystemTerminator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.SignalHandler;

@DisplayName("SystemTerminator Unit Test")
public class SystemTerminatorTest {

    @Test
    @DisplayName("System.exit should be called with the expected status code")
    void testHandleTerminationCallsSystemExit() throws Exception {
        // given
        SystemTerminator terminator = new SystemTerminator();
        int expectedStatusCode = EXIT_CODE;
        SignalHandler handler = terminator.handleTermination(expectedStatusCode);

        // when
        int actualExitCode = catchSystemExit(() -> handler.handle(null));

        // then
        assertThat(actualExitCode).isEqualTo(expectedStatusCode);
    }
}
