package io.jeyong.test.integration;

import static io.jeyong.test.cleanup.ContextClosedEventHandler.CONTEXT_CLOSED_EVENT_LOG;
import static io.jeyong.test.cleanup.PreDestroyHandler.PRE_DESTROY_LOG;
import static io.jeyong.test.cleanup.ShutdownHookHandler.SHUTDOWN_HOOK_LOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@SpringBootTest
@DisplayName("SigtermHandler Integration Test")
public class SigtermHandlerTest {

    private static ImageFromDockerfile dockerImage;

    @BeforeAll
    static void setUp() {
        dockerImage = buildImage();
    }

    @Test
    @DisplayName("Container should exit with code 0 on SIGTERM")
    void testExitCode() throws Exception {
        // given
        GenericContainer<?> container = new GenericContainer<>(dockerImage)
                .waitingFor(forLogMessage(".*Started TestApplication.*\\n", 1));
        container.start();

        // when
        sendSigtermToContainer(container);

        // then
        Long exitCode = container.getCurrentContainerInfo().getState().getExitCodeLong();
        assertThat(exitCode).isEqualTo(0);
    }

    @Test
    @DisplayName("Container should exit with cleanup performed on SIGTERM")
    void testCleanUp() throws Exception {
        // given
        GenericContainer<?> container = new GenericContainer<>(dockerImage)
                .waitingFor(forLogMessage(".*Started TestApplication.*\\n", 1));
        container.start();

        // when
        sendSigtermToContainer(container);

        // then
        String logs = container.getLogs();
        assertSoftly(softly -> {
            softly.assertThat(logs).contains(PRE_DESTROY_LOG);
            softly.assertThat(logs).contains(CONTEXT_CLOSED_EVENT_LOG);
            softly.assertThat(logs).contains(SHUTDOWN_HOOK_LOG);
        });
    }

    private static ImageFromDockerfile buildImage() {
        Path tempDir = createTempDirectory();
        return new ImageFromDockerfile()
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
    }

    private static Path createTempDirectory() {
        try {
            Path tempDir = Files.createTempDirectory("docker-context");
            copyFile("../gradlew", tempDir.resolve("gradlew"));
            copyDirectory("../gradle", tempDir.resolve("gradle"));
            copyFile("../settings.gradle", tempDir.resolve("settings.gradle"));
            copyDirectory("../handler", tempDir.resolve("handler"));
            copyDirectory("../test", tempDir.resolve("test"));
            return tempDir;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temp directory for Docker context", e);
        }
    }

    private static void copyFile(String source, Path destination) throws Exception {
        FileUtils.copyFile(new File(source), destination.toFile());
    }

    private static void copyDirectory(String source, Path destination) throws Exception {
        FileUtils.copyDirectory(new File(source), destination.toFile());
    }

    private static void sendSigtermToContainer(GenericContainer<?> container) throws Exception {
        String containerId = container.getContainerId();
        container.getDockerClient()
                .killContainerCmd(containerId)
                .withSignal("SIGTERM")
                .exec();

        Thread.sleep(1000);
    }
}
