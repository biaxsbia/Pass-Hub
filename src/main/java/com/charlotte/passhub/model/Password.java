package com.charlotte.passhub.model;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;


import java.time.LocalDateTime;

@Getter
@Setter
@DynamoDbBean
public class Password {
    private String id;
    private String serviceName;
    private String username;
    private String email;
    private String encryptedPassword;
    private String notes;
    private String createdAt;
    private String updatedAt;
    private String userId;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "user-index")
    public String getUserId() {
        return userId;
    }
}