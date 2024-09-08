package com.test.questionsinterviewautomation.util;

import com.test.questionsinterviewautomation.constant.ApplicationConstant;
import com.test.questionsinterviewautomation.service.FileService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class ApplicationUtil {
    private final FileService fileService;

    public ApplicationUtil(FileService fileService) {
        this.fileService = fileService;
    }

    public void updateHistoryResponse(String promptMessage) throws URISyntaxException, IOException {

        JsonObject promptMessageToJson = modifyPromptMessageToJson(promptMessage);
        String historyResponse = String.valueOf(fileService.readJsonFromFile("E:/questions-interview-automation/src/main/resources/prompt-response-history.json"));
        JsonObject historyResponseToJson;

        if (historyResponse.isEmpty()) {
            historyResponseToJson = new JsonObject();
            promptMessageToJson.stream()
                    .forEach(entry -> {
                        JsonArray x = ((JsonArray) entry.getValue()).stream()
                                .map(JsonObject::mapFrom)
                                .map(json -> json.getString("question"))
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                        historyResponseToJson.put(entry.getKey(), x);
                    });
        } else {
            historyResponseToJson = new JsonObject(historyResponse);
            promptMessageToJson.stream()
                    .forEach(entry -> {
                        JsonArray x = ((JsonArray) entry.getValue()).stream()
                                .map(JsonObject::mapFrom)
                                .map(json -> json.getString("question"))
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                        historyResponseToJson.put(entry.getKey(), historyResponseToJson.getJsonArray(entry.getKey(), new JsonArray()).addAll(x));
                    });
        }
        log.info("History response {}", historyResponseToJson);
        fileService.writeJsonToFile("E:/questions-interview-automation/src/main/resources/prompt-response-history.json", historyResponseToJson);
    }

    public String getMessageForEmailBody(String promptMessage) {
        JsonObject promptMessageToJson = modifyPromptMessageToJson(promptMessage);
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html>")
                .append("<head>")
//                .append("    <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap\">")
                .append("    <style>")
                .append("        body {")
                .append("            font-family: 'Comic Sans MS', 'Comic Sans', cursive;")
                .append("        }")
                .append("    </style>")
                .append("</head>")
                .append("<body>");

        promptMessageToJson.stream()
                .forEach(entry -> {
                    emailBody.append("<b><u>").append(entry.getKey().split("(?<=[a-z])(?=[A-Z])")[0].toUpperCase())
                            .append(" ").append(entry.getKey().split("(?<=[a-z])(?=[A-Z])")[1].toUpperCase())
                            .append("</u></b>").append("<br>").append("<br>");
                    ((JsonArray) entry.getValue()).stream()
                            .map(JsonObject::mapFrom)
                            .forEach(json -> {
                                if (ApplicationConstant.typeList.contains(entry.getKey())) {
                                    emailBody.append("<b>").append("Type :: ").append("</b>").append("<b>").append(json.getString("type").toUpperCase()).append("</b>").append("<br>");
                                }
                                emailBody.append("<b>").append("Q :: ").append("</b>").append("<b>").append(json.getString("question")).append("</b>").append("<br>");
                                emailBody.append("<b>").append("K :: ").append("</b>").append("<br>");
                                json.getJsonArray("keyPoints")
                                        .stream()
                                        .map(String::valueOf)
                                        .forEach(keyPoint -> {
                                            emailBody.append("&emsp;• ").append(keyPoint).append("<br>");
                                        });
                                emailBody.append("<br>");
                            });
                    emailBody.append("<br>");
                });

        emailBody.append("</body>")
                .append("</html>");
        return emailBody.toString();
    }


    public JsonObject modifyPromptMessageToJson(String promptMessage) {
        promptMessage = promptMessage.replace("```", "").replace("json", "");
        return new JsonObject(promptMessage);
    }
}
