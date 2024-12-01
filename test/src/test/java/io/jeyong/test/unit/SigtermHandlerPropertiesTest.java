package io.jeyong.test.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.jeyong.handler.SigtermHandlerProperties;
import io.jeyong.test.TestApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@DisplayName("SigtermHandlerProperties Unit Test")
class SigtermHandlerPropertiesTest {

    private static final int EXIT_CODE = 10;
    private static final String TERMINATION_MESSAGE_PATH = "/termination-message.message";
    private static final String TERMINATION_MESSAGE = "Test termination message";

    @Nested
    @SpringBootTest(
            classes = TestApplication.class,
            properties = {
                    "kubernetes.sigterm-handler.enabled=true",
                    "kubernetes.sigterm-handler.exit-code=" + EXIT_CODE,
                    "kubernetes.sigterm-handler.termination-message-path=" + TERMINATION_MESSAGE_PATH,
                    "kubernetes.sigterm-handler.termination-message=" + TERMINATION_MESSAGE,
            }
    )
    @DisplayName("handler enabled")
    class EnabledTests {

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private SigtermHandlerProperties properties;

        @Test
        @DisplayName("Register Configuration with the configured properties")
        void testRegisterConfiguration() {
            // given & when
            boolean beanExists = applicationContext.containsBeanDefinition(
                    "io.jeyong.handler.SigtermHandlerConfiguration");

            // then
            assertSoftly(softly -> {
                softly.assertThat(beanExists).isTrue();
                softly.assertThat(properties.getExitCode()).isEqualTo(EXIT_CODE);
                softly.assertThat(properties.getTerminationMessagePath()).isEqualTo(TERMINATION_MESSAGE_PATH);
                softly.assertThat(properties.getTerminationMessage()).isEqualTo(TERMINATION_MESSAGE);
            });
        }
    }

    @Nested
    @SpringBootTest(
            classes = TestApplication.class,
            properties = {
                    "kubernetes.sigterm-handler.enabled=false",
            }
    )
    @DisplayName("handler disabled")
    class DisabledTests {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        @DisplayName("Does not register Configuration")
        void testDoesNotRegisterConfiguration() {
            // given & when
            boolean beanExists = applicationContext.containsBeanDefinition(
                    "io.jeyong.handler.SigtermHandlerConfiguration");

            // then
            assertThat(beanExists).isFalse();
        }
    }
}
