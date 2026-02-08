package com.charlotte.passhub.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@DynamoDbBean
public class Password {

    private String userId;
    private String passwordId;
    private String service;
    private String login;
    private String encryptedPassword;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
}
