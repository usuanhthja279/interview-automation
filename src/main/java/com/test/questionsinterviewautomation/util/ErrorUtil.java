package com.test.questionsinterviewautomation.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ErrorUtil {

    public static void printStackTraceDepth(String topClassName, Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        List<String> stackTraceList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stackTraceList.add(stackTrace[i].toString());
        }
        log.info("Error occured at : {} ::: Error stack trace: {}", topClassName, stackTraceList);
    }
}
