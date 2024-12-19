package io.jeyong.test.unit;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.assertj.core.api.Assertions.assertThat;

import io.jeyong.handler.ApplicationTerminator;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@DisplayName("ApplicationTerminator Unit Test")
public class ApplicationTerminatorTest {

    @Test
    @DisplayName("FileUtils.writeToFile should create a file with correct content")
    void testFileCreationAndContent() throws Exception {
        // given
        File tempFile = Files.createTempFile("termination-", ".txt").toFile();
        String terminationMessagePath = tempFile.getAbsolutePath();
        String expectedMessage = "Test termination message";

        ApplicationTerminator terminator = new ApplicationTerminator(terminationMessagePath, expectedMessage) {

            @Override
            protected int getExitCode() {
                return 0;
            }
        };

        // when
        catchSystemExit(() -> terminator.handleTermination().handle(new Signal("TERM")));

        // then
        assertThat(Files.readString(tempFile.toPath())).isEqualTo(expectedMessage);

        // Clean up
        Files.deleteIfExists(tempFile.toPath());
    }

    @Test
    @DisplayName("System.exit should be called with the expected exit code")
    void testHandleTerminationCallsSystemExit() throws Exception {
        // given
        int expectedExitCode = 0;
        ApplicationTerminator terminator = new ApplicationTerminator(null, null) {

            @Override
            protected int getExitCode() {
                return expectedExitCode;
            }
        };
        SignalHandler handler = terminator.handleTermination();

        // when
        int actualExitCode = catchSystemExit(() -> handler.handle(new Signal("TERM")));

        // then
        assertThat(actualExitCode).isEqualTo(expectedExitCode);
    }
}
