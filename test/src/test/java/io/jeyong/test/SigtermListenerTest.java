package io.jeyong.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@SpringBootTest
public class SigtermListenerTest {

    private final Logger logger = LoggerFactory.getLogger(SigtermListenerTest.class);

    @Test
    void testSigtermHandling() throws Exception {
        // given
        Path tempDir = Files.createTempDirectory("docker-context");
        copyFile("../gradlew", tempDir.resolve("gradlew"));
        copyDirectory("../gradle", tempDir.resolve("gradle"));
        copyFile("../settings.gradle", tempDir.resolve("settings.gradle"));
        copyDirectory("../handler", tempDir.resolve("handler"));
        copyDirectory("../test", tempDir.resolve("test"));

        ImageFromDockerfile image = new ImageFromDockerfile()
                .withFileFromPath(".", tempDir)
                .withDockerfileFromBuilder(builder -> {
                    builder.from("eclipse-temurin:17")
                            .workDir("/app")
                            .copy("gradlew", "/app/gradlew")
                            .copy("gradle", "/app/gradle")
                            .copy("settings.gradle", "/app/settings.gradle")
                            .copy("handler", "/app/handler")
                            .copy("test", "/app/test")
                            .run("chmod +x ./gradlew")
                            .run("./gradlew bootJar")
                            .cmd("java", "-jar", "/app/test/build/libs/test-0.0.1-SNAPSHOT.jar")
                            .build();
                });

        // when
        try (GenericContainer<?> container = new GenericContainer<>(image)
                .withLogConsumer(new Slf4jLogConsumer(logger))) {
            container.start();
            sendSigtermToContainer(container);

            // then
            Integer exitCode = container.getCurrentContainerInfo().getState().getExitCode();
            assertThat(exitCode).isEqualTo(0);
        }
    }

    private void sendSigtermToContainer(GenericContainer<?> container) {
        String containerId = container.getContainerId();
        container.getDockerClient()
                .killContainerCmd(containerId)
                .withSignal("SIGTERM")
                .exec();
    }

    private void copyFile(String source, Path destination) throws Exception {
        FileUtils.copyFile(new File(source), destination.toFile());
    }

    private void copyDirectory(String source, Path destination) throws Exception {
        FileUtils.copyDirectory(new File(source), destination.toFile());
    }
}
