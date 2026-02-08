package com.charlotte.passhub.repository;

import com.charlotte.passhub.model.Password;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PasswordRepository {
    private final DynamoDbTable<Password> passwordTable;

    public PasswordRepository(DynamoDbEnhancedClient enhancedClient) {
        this.passwordTable = enhancedClient.table("Passwords", TableSchema.fromBean(Password.class));
    }

    public Password save(Password password) {
        passwordTable.putItem(password);
        return password;
    }

    public List<Password> findAll() {
        List<Password> passwords = new ArrayList<>();
        passwordTable.scan().items().forEach(passwords::add);
        return passwords;
    }

    public Optional<Password> findById(String id) {
        return Optional.ofNullable(passwordTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }

    public List<Password> findByUserId(String userId) {
        DynamoDbIndex<Password> userIndex = passwordTable.index("user-index");
        List<Password> passwords = new ArrayList<>();
        userIndex.query(QueryConditional.keyEqualTo(k -> k.partitionValue(userId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .forEach(passwords::add);
        return passwords;
    }

    public void deleteById(String id) {
        passwordTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }
}