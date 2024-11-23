package io.jeyong.test.unit;

import static io.jeyong.handler.SignalHandlerRegistrar.SIGNAL_TYPE;
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
        SignalHandler mockSignalHandler = signal -> System.out.println("Mock signal handler");
        ApplicationTerminator applicationTerminator = status -> mockSignalHandler;
        SignalHandlerRegistrar registrar = new SignalHandlerRegistrar(applicationTerminator);

        // when
        registrar.registerHandler();

        Signal registeredSignal = new Signal(SIGNAL_TYPE);
        SignalHandler registeredHandler = Signal.handle(registeredSignal, null);

        // then
        assertThat(registeredHandler).isEqualTo(mockSignalHandler);
    }
}
