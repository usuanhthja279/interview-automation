package com.test.questionsinterviewautomation.repository;

import com.test.questionsinterviewautomation.entity.PromptResponseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PromtDataBaseRepository extends MongoRepository<PromptResponseEntity, String> {
    // TODO: Implement this repository
}
