package io.jeyong.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static void writeToFile(final String filePath, final String message) {
        if (Strings.isBlank(filePath)) {
            return;
        }

        final File file = new File(filePath);
        try {
            createParentDirectories(file);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(message);
            }
        } catch (IOException e) {
            logger.error("Failed to write to file {}: {}", filePath, e.getMessage());
        }
    }

    private static void createParentDirectories(final File file) throws IOException {
        final File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create parent directories for " + file.getAbsolutePath());
        }
    }
}
