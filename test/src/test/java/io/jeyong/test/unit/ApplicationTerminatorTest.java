package io.jeyong.test.unit;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.assertj.core.api.Assertions.assertThat;

import io.jeyong.handler.ApplicationTerminator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.SignalHandler;

@DisplayName("ApplicationTerminator Unit Test")
public class ApplicationTerminatorTest {

    @Test
    @DisplayName("System.exit should be called with the expected exit code")
    void testHandleTerminationCallsSystemExit() throws Exception {
        // given
        int expectedExitCode = 0;
        ApplicationTerminator terminator = new ApplicationTerminator() {
            
            @Override
            protected int getExitCode() {
                return expectedExitCode;
            }
        };
        SignalHandler handler = terminator.handleTermination();

        // when
        int actualExitCode = catchSystemExit(() -> handler.handle(null));

        // then
        assertThat(actualExitCode).isEqualTo(expectedExitCode);
    }
}
