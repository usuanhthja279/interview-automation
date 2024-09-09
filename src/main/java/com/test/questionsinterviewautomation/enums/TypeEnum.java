package com.test.questionsinterviewautomation.enums;

import io.vertx.core.json.JsonObject;

import java.util.List;

public enum TypeEnum {
    PYTHON(new JsonObject().put("questions", List.of("python", "pandas", "numpy", "django", "flask", "dsa"))
            .put("exercise", List.of("python", "pandas", "numpy", "dsa"))),
    WEB_DEVELOPMENT(new JsonObject().put("questions", List.of("html", "css", "javascript", "dsa", "react", "node", "express", "mongo"))
            .put("exercise", List.of("html", "css", "javascript", "react", "node", "dsa"))),
    SQL(new JsonObject().put("questions", List.of("sql", "mysql", "postgresql", "dsa"))
            .put("exercise", List.of("sql", "mysql", "dsa"))),
    JAVA(new JsonObject().put("questions", List.of("java", "spring", "springBoot", "dsa", "streams", "lambdas", "vertx", "completablefuture"))
            .put("exercise", List.of("java", "spring", "springBoot", "dsa", "streams", "lambdas", "vertx", "completablefuture"))),
    DATA_SCIENCE(new JsonObject().put("questions", List.of("python", "pandas", "numpy", "sql", "machineLearning", "deepLearning", "dsa"))
            .put("exercise", List.of("python", "pandas", "numpy", "sql", "machineLearning", "deepLearning", "dsa"))),
    SYSTEM_DESIGN(new JsonObject().put("questions", List.of("systemDesign", "dsa"))
            .put("exercise", List.of("systemDesign", "dsa")));


    private final JsonObject typeList;

    TypeEnum(JsonObject typeList) {
        this.typeList = typeList;
    }
    
    public static JsonObject getTypeList(String type) {
        TypeEnum typeEnum = TypeEnum.valueOf(type.toUpperCase());
        return typeEnum.typeList;
    }
}
