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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

@SpringBootTest
@DisplayName("SigtermHandler Integration Test")
public class SigtermHandlerTest {

    private static final int EXIT_CODE = 10;
    private static final String TERMINATION_MESSAGE_PATH = "/app/termination-message.message";
    private static final String TERMINATION_MESSAGE = "Test termination message";

    private static Path codeDir;
    private static ImageFromDockerfile dockerImage;

    @BeforeAll
    static void setUp() throws Exception {
        codeDir = createCodeDirectory();
        dockerImage = buildImage(codeDir);
    }

    @AfterAll
    static void tearDown() throws Exception {
        FileUtils.deleteDirectory(codeDir.toFile());
    }

    @Test
    @DisplayName("Container should exit with expected code on SIGTERM")
    void testExitCode() throws Exception {
        // given
        GenericContainer<?> container = new GenericContainer<>(dockerImage)
                .waitingFor(forLogMessage(".*Started TestApplication.*\\n", 1));
        container.start();

        // when
        sendSigtermToContainer(container);

        // then
        Long exitCode = container.getCurrentContainerInfo().getState().getExitCodeLong();
        assertThat(exitCode).isEqualTo(EXIT_CODE);
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

    @Test
    @DisplayName("Check termination message file inside container")
    void testTerminationMessageFile() throws Exception {
        // given
        GenericContainer<?> container = new GenericContainer<>(dockerImage)
                .waitingFor(forLogMessage(".*Started TestApplication.*\\n", 1));
        container.start();

        // when
        sendSigtermToContainer(container);

        // then
        String fileContent = container.copyFileFromContainer(
                TERMINATION_MESSAGE_PATH,
                content -> new String(content.readAllBytes())
        );
        assertThat(fileContent).isEqualTo(TERMINATION_MESSAGE);
    }

    private static Path createCodeDirectory() throws Exception {
        Path codeDir = Files.createTempDirectory("docker-context");
        copyFile("../gradlew", codeDir.resolve("gradlew"));
        copyDirectory("../gradle", codeDir.resolve("gradle"));
        copyFile("../settings.gradle", codeDir.resolve("settings.gradle"));
        copyDirectory("../handler", codeDir.resolve("handler"));
        copyDirectory("../test", codeDir.resolve("test"));
        createApplicationYaml(codeDir.resolve("test/src/main/resources"));
        return codeDir;
    }

    private static void createApplicationYaml(Path resourcesDir) throws Exception {
        Files.createDirectories(resourcesDir);
        Path applicationYaml = resourcesDir.resolve("application.yml");
        String yamlContent = String.format("""
                kubernetes:
                  sigterm-handler:
                    exit-code: %d
                    termination-message-path: %s
                    termination-message: %s
                """, EXIT_CODE, TERMINATION_MESSAGE_PATH, TERMINATION_MESSAGE);
        Files.writeString(applicationYaml, yamlContent);
    }

    private static ImageFromDockerfile buildImage(Path sourceCodeDir) {
        return new ImageFromDockerfile()
                .withFileFromPath(".", sourceCodeDir)
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

        while (container.isRunning()) {
            Thread.sleep(1000);
        }
    }
}
