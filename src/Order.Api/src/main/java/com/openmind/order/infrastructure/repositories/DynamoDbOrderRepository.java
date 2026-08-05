package com.openmind.order.infrastructure.repositories;

import tools.jackson.databind.ObjectMapper;
import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.domain.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DynamoDbOrderRepository implements OrderRepository {

    private final DynamoDbClient dynamoDbClient;
    private final OrderDynamoMapper mapper;
    private final String tableName;

    public DynamoDbOrderRepository(DynamoDbClient dynamoDbClient, ObjectMapper objectMapper,
                                    @Value("${ORDERS_TABLE:Orders}") String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.mapper = new OrderDynamoMapper(objectMapper);
        this.tableName = tableName;
    }

    @Override
    public Optional<OrderAggregate> getById(String orderId) {
        GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.fromS(orderId)))
                .build());

        return response.hasItem() ? Optional.of(mapper.toOrder(response.item())) : Optional.empty();
    }

    @Override
    public List<OrderAggregate> getByCustomerId(String customerId) {
        QueryResponse response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .indexName("CustomerIdIndex")
                .keyConditionExpression("customerId = :customerId")
                .expressionAttributeValues(Map.of(":customerId", AttributeValue.fromS(customerId)))
                .build());

        return response.items().stream().map(mapper::toOrder).toList();
    }

    @Override
    public List<OrderAggregate> getByCustomerIdAndStatus(String customerId, OrderStatus status) {
        Map<String, String> names = Map.of("#status", "status");
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":customerId", AttributeValue.fromS(customerId));
        values.put(":status", AttributeValue.fromS(status.name()));

        QueryResponse response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .indexName("CustomerIdIndex")
                .keyConditionExpression("customerId = :customerId")
                .filterExpression("#status = :status")
                .expressionAttributeNames(names)
                .expressionAttributeValues(values)
                .build());

        return response.items().stream().map(mapper::toOrder).toList();
    }

    @Override
    public List<OrderAggregate> getByDate(LocalDate date) {
        QueryResponse response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .indexName("OrderDateIndex")
                .keyConditionExpression("orderDate = :date")
                .expressionAttributeValues(Map.of(":date", AttributeValue.fromS(date.format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .scanIndexForward(false)
                .build());

        return response.items().stream().map(mapper::toOrder).toList();
    }

    @Override
    public List<OrderAggregate> getAll() {
        ScanResponse response = dynamoDbClient.scan(ScanRequest.builder().tableName(tableName).build());
        return response.items().stream().map(mapper::toOrder).toList();
    }

    @Override
    public OrderAggregate add(OrderAggregate order) {
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(mapper.toAttributeValues(order))
                .build());
        return order;
    }

    @Override
    public OrderAggregate update(OrderAggregate order) {
        return add(order);
    }

    @Override
    public void delete(String orderId) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.fromS(orderId)))
                .build());
    }
}
