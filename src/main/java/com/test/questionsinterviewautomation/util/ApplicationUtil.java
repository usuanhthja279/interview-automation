package com.test.questionsinterviewautomation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.questionsinterviewautomation.constant.ApplicationConstant;
import com.test.questionsinterviewautomation.enums.TypeEnum;
import com.test.questionsinterviewautomation.service.FileService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApplicationUtil {
    private final FileService fileService;

    public ApplicationUtil(FileService fileService) {
        this.fileService = fileService;
    }

    public static List<String> getMailsForLevels(List<String> mails, JsonObject levels, String level) {
        return levels.getJsonArray("levels").stream()
                .map(JsonObject::mapFrom)
                .filter(json -> mails.contains(json.getString("mail")))
                .filter(json -> level.equals(json.getString("level")))
                .map(json -> json.getString("mail"))
                .toList();
    }

    public static Set<String> getDistinctLevelsOnMails(List<String> mails, JsonObject levels) {
        return levels.getJsonArray("levels").stream()
                .map(JsonObject::mapFrom)
                .filter(json -> mails.contains(json.getString("mail")))
                .map(json -> json.getString("level"))
                .collect(Collectors.toSet());
    }

    public void updateHistoryResponse(String promptMessage, String level, String mail) {

        JsonObject promptMessageToJson = modifyPromptMessageToJson(promptMessage);
        String historyResponse = String.valueOf(fileService.readJsonFromFile("E:/questions-interview-automation/src/main/resources/prompt-response-history.json"));
        JsonObject historyResponseToJson;
        log.info("History response before update: {}", historyResponse);

        //{"data": [{"mail": "mail", "level": "level", "questions": {"type": "question"}}]}

        if (historyResponse.isEmpty() || "{}".equals(historyResponse)) {
            JsonObject questionsExtractions = new JsonObject();
            promptMessageToJson.stream()
                    .forEach(entry -> {
                        JsonArray x = ((JsonArray) entry.getValue()).stream()
                                .map(JsonObject::mapFrom)
                                .map(json -> json.getString("question") != null ? json.getString("question") : json.getString("exercise"))
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                        questionsExtractions.put(entry.getKey(), x);
                    });

            historyResponseToJson = new JsonObject().put("data", new JsonArray().add(new JsonObject()
                    .put("mail", mail)
                    .put("level", level)
                    .put("questions", questionsExtractions)));
//            promptMessageToJson.stream()
//                    .forEach(entry -> {
//                        JsonArray x = ((JsonArray) entry.getValue()).stream()
//                                .map(JsonObject::mapFrom)
//                                .map(json -> json.getString("question"))
//                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
//                        historyResponseToJson.put(entry.getKey(), x);
//                    });
        } else if (new JsonObject(historyResponse).getJsonArray("data")
                        .stream()
                        .map(JsonObject::mapFrom)
                        .noneMatch(x -> Objects.equals(mail, x.getString("mail")))) {
            JsonObject questionsExtractions = new JsonObject();
            promptMessageToJson.stream()
                    .forEach(entry -> {
                        JsonArray x = ((JsonArray) entry.getValue()).stream()
                                .map(JsonObject::mapFrom)
                                .map(json -> json.getString("question") != null ? json.getString("question") : json.getString("exercise"))
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                        questionsExtractions.put(entry.getKey(), x);
                    });

            historyResponseToJson = new JsonObject(historyResponse);
            JsonObject newData = new JsonObject()
                    .put("mail", mail)
                    .put("level", level)
                    .put("questions", questionsExtractions);
            historyResponseToJson.getJsonArray("data").add(newData);
        } else {
            historyResponseToJson = new JsonObject(historyResponse);
            promptMessageToJson.stream()
                    .map(JsonObject::mapFrom)
                    .filter(entry -> Objects.equals(mail, historyResponseToJson.getString("mail")))
                    .flatMap(entry -> entry.getJsonObject("questions").stream())
                    .forEach(entry -> {
                        JsonArray x = ((JsonArray) entry.getValue()).stream()
                                .map(JsonObject::mapFrom)
                                .map(json -> json.getString("question"))
                                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
                        historyResponseToJson.put(entry.getKey(), historyResponseToJson.getJsonArray(entry.getKey(), new JsonArray()).addAll(x));
                    });
        }
        log.info("History response update for: mail: {} level: {} ::: {}", mail, level, historyResponseToJson);
        fileService.writeJsonToFile("E:/questions-interview-automation/src/main/resources/prompt-response-history.json", historyResponseToJson);
    }

    public String getMessageForEmailBody(String promptResponse, String level) {
        JsonObject promptMessageToJson = modifyPromptMessageToJson(promptResponse);
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
                .append("<body>")
                .append("<b>")
                .append(level)
                .append("</b>")
                .append("<br><br>");

        promptMessageToJson.stream()
                .forEach(entry -> {
                    emailBody.append("<b><u>").append(entry.getKey().split("(?<=[a-z])(?=[A-Z])")[0].toUpperCase())
                            .append(" ").append(entry.getKey().split("(?<=[a-z])(?=[A-Z])")[1].toUpperCase())
                            .append("</u></b>").append("<br>").append("<br>");
                    ((JsonArray) entry.getValue()).stream()
                            .map(JsonObject::mapFrom)
                            .forEach(json -> {
//                                if (ApplicationConstant.typeList.contains(entry.getKey())) {
//                                    emailBody.append("<b>").append("Type :: ").append("</b>").append("<b>").append(json.getString("type").toUpperCase()).append("</b>").append("<br>");
//                                }
                                emailBody.append("<b>").append("Q :: ").append("</b>").append("<b>")
                                        .append(json.getString("question") != null ? json.getString("question") : json.getString("exercise")).append("</b>").append("<br>");
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

    public String createCustomPrompt(List<String> types, String levelValue) {
        StringBuilder prompt = new StringBuilder();
        types.forEach(type -> {
            JsonObject promptTypes = TypeEnum.getTypeList(type);

            JsonArray problemKeys = promptTypes.getJsonArray("questions");
            JsonArray exerciseKeys = promptTypes.getJsonArray("exercise");

            problemKeys.stream()
                    .map(x -> (String) x)
                    .forEach(problemType -> {
                        prompt.append("Generate 5 unique ").append(problemType)
                                .append(" interview questions with difficulty level: ")
                                .append(levelValue).append(", in this format: ")
                                .append(problemType).append("Questions")
                                .append(": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}]")
                                .append("\n");
                    });
            exerciseKeys.stream()
                    .map(x -> (String) x)
                    .forEach(exerciseType -> {
                        prompt.append("Generate 3 unique ").append(exerciseType)
                                .append(" interview exercises with difficulty level: ")
                                .append(levelValue).append(", in this format: ")
                                .append(exerciseType).append("Exercise")
                                .append(": [{\"exercise\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}]")
                                .append("\n");
                    });
        });

        prompt.append("Please add key points of answer for the questions.").append("\n")
                .append("Please provide the response in JSON text format.");

        log.info("Prompt Request for Level: {} ::: {}", levelValue, prompt);
        return prompt.toString();
    }
}
