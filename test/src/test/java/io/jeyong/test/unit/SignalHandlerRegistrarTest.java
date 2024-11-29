package io.jeyong.test.unit;

import static org.assertj.core.api.Assertions.assertThat;

import io.jeyong.handler.ApplicationTerminator;
import io.jeyong.handler.SignalHandlerRegistrar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@DisplayName("SignalHandlerRegistrar Unit Test")
public class SignalHandlerRegistrarTest {

    @Test
    @DisplayName("SignalHandler should be registered correctly")
    void testSignalHandlerRegistration() {
        // given
        String signalType = "TERM";
        int exitCode = 0;
        SignalHandler expectedHandler = signal -> System.exit(exitCode);
        ApplicationTerminator terminator = new ApplicationTerminator(null, null) {

            @Override
            public SignalHandler handleTermination() {
                return expectedHandler;
            }

            @Override
            protected int getExitCode() {
                return exitCode;
            }
        };
        SignalHandlerRegistrar registrar = new SignalHandlerRegistrar(terminator, signalType);

        // when
        registrar.registerHandler();

        Signal registeredSignal = new Signal(signalType);
        SignalHandler registeredHandler = Signal.handle(registeredSignal, null);

        // then
        assertThat(registeredHandler).isEqualTo(expectedHandler);
    }
}
