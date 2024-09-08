package com.test.questionsinterviewautomation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileService {
    private final ObjectMapper objectMapper;

    public FileService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeJsonToFile(String path, JsonObject jsonObject) {
        try {
            // Write the content to the file
            Path filePath = Paths.get(path);
            log.info("File to be written: {}", filePath.toAbsolutePath());
            Files.writeString(filePath, jsonObject.encodePrettily());
        } catch (Exception e) {
            log.error("Error writing file: {}", e.getMessage());
            throw new RuntimeException("Error writing file: " + e.getMessage());
        }
    }

    public JsonObject readJsonFromFile(String path) {
        try {
            // Read the file and return the content
            File file = new File(path);
            log.info("File to be read: {}", file.getAbsolutePath());
            byte[] data = Files.readAllBytes(file.toPath());
            if (data.length > 0) {
                return new JsonObject(new String(data));
            }
            return new JsonObject();
        } catch (Exception e) {
            log.error("Error reading file: {}", e.getMessage());
        }
        return null;
    }
}
