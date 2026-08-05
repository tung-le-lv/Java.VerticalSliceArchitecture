package com.openmind.payment.infrastructure.repositories;

import com.openmind.payment.domain.entities.PaymentAggregate;
import com.openmind.payment.domain.enums.PaymentStatus;
import com.openmind.payment.domain.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class DynamoDbPaymentRepository implements PaymentRepository
{

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public DynamoDbPaymentRepository(DynamoDbClient dynamoDbClient,
            @Value("${PAYMENTS_TABLE:Payments}") String tableName)
    {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    @Override
    public Optional<PaymentAggregate> getById(String paymentId)
    {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder().tableName(tableName)
                .key(Map.of("id", AttributeValue.fromS(paymentId))).build());

        return response.hasItem() ? Optional.of(toPayment(response.item())) : Optional.empty();
    }

    @Override
    public Optional<PaymentAggregate> getByOrderId(String orderId)
    {
        QueryResponse response = dynamoDbClient.query(QueryRequest.builder().tableName(tableName)
                .indexName("OrderIdIndex").keyConditionExpression("orderId = :orderId")
                .expressionAttributeValues(Map.of(":orderId", AttributeValue.fromS(orderId))).limit(1).build());

        return response.items().isEmpty() ? Optional.empty() : Optional.of(toPayment(response.items().get(0)));
    }

    @Override
    public PaymentAggregate add(PaymentAggregate payment)
    {
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(toAttributeValues(payment)).build());
        return payment;
    }

    private static Map<String, AttributeValue> toAttributeValues(PaymentAggregate payment)
    {
        Map<String, AttributeValue> item = new LinkedHashMap<>();
        item.put("id", AttributeValue.fromS(payment.getId()));
        item.put("orderId", AttributeValue.fromS(payment.getOrderId()));
        item.put("customerId", AttributeValue.fromS(payment.getCustomerId()));
        item.put("amount", AttributeValue.fromN(payment.getAmount().toPlainString()));
        item.put("status", AttributeValue.fromS(payment.getStatus().name()));
        item.put("failureReason",
                AttributeValue.fromS(payment.getFailureReason() != null ? payment.getFailureReason() : ""));
        item.put("createdAt", AttributeValue.fromS(payment.getCreatedAt().toString()));
        item.put("processedAt", AttributeValue.fromS(payment.getProcessedAt().toString()));
        return item;
    }

    private static PaymentAggregate toPayment(Map<String, AttributeValue> item)
    {
        AttributeValue failureReason = item.get("failureReason");
        return PaymentAggregate.reconstitute(item.get("id").s(), item.get("orderId").s(), item.get("customerId").s(),
                new BigDecimal(item.get("amount").n()), PaymentStatus.valueOf(item.get("status").s()),
                failureReason != null && failureReason.s() != null && !failureReason.s().isEmpty()
                        ? failureReason.s()
                        : null,
                Instant.parse(item.get("createdAt").s()), Instant.parse(item.get("processedAt").s()));
    }
}
