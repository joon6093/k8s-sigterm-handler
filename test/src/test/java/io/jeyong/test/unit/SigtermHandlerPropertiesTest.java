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

    @Nested
    @SpringBootTest(
            classes = TestApplication.class,
            properties = {
                    "kubernetes.handler.enabled=true",
                    "kubernetes.handler.exit-code=1000"
            }
    )
    @DisplayName("When enabled")
    class EnabledTests {

        @Autowired
        private ApplicationContext context;

        @Autowired
        private SigtermHandlerProperties properties;

        @Test
        @DisplayName("Registers ApplicationTerminator with the configured exit code")
        void registersApplicationTerminator() {
            // given & when
            boolean beanExists = context.containsBeanDefinition("applicationTerminator");

            // then
            assertSoftly(softly -> {
                softly.assertThat(beanExists).isTrue();
                softly.assertThat(properties.getExitCode()).isEqualTo(1000);
            });
        }
    }

    @Nested
    @SpringBootTest(
            classes = TestApplication.class,
            properties = {
                    "kubernetes.handler.enabled=false",
                    "kubernetes.handler.exit-code=2"
            }
    )
    @DisplayName("When disabled")
    class DisabledTests {

        @Autowired
        private ApplicationContext context;

        @Test
        @DisplayName("Does not register ApplicationTerminator")
        void doesNotRegisterApplicationTerminator() {
            // given & when
            boolean beanExists = context.containsBeanDefinition("applicationTerminator");

            // then
            assertThat(beanExists).isFalse();
        }
    }
}
