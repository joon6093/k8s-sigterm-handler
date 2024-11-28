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
                    "kubernetes.sigterm-handler.enabled=true",
                    "kubernetes.sigterm-handler.exit-code=1000"
            }
    )
    @DisplayName("handler enabled")
    class EnabledTests {

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private SigtermHandlerProperties sigtermHandlerProperties;

        @Test
        @DisplayName("Register Configuration with the configured exit code")
        void registerConfiguration() {
            // given & when
            boolean beanExists = applicationContext.containsBeanDefinition(
                    "io.jeyong.handler.SigtermHandlerConfiguration");

            // then
            assertSoftly(softly -> {
                softly.assertThat(beanExists).isTrue();
                softly.assertThat(sigtermHandlerProperties.getExitCode()).isEqualTo(1000);
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
        void doesNotRegisterConfiguration() {
            // given & when
            boolean beanExists = applicationContext.containsBeanDefinition(
                    "io.jeyong.handler.SigtermHandlerConfiguration");

            // then
            assertThat(beanExists).isFalse();
        }
    }
}
