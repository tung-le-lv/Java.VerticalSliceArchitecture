package com.openmind.order.infrastructure.repositories;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.openmind.order.domain.entities.OrderAggregate;
import com.openmind.order.domain.enums.OrderStatus;
import com.openmind.order.domain.valueobjects.OrderItem;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class OrderDynamoMapper
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final ObjectMapper objectMapper;

    OrderDynamoMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    OrderAggregate toOrder(Map<String, AttributeValue> item)
    {
        try
        {
            List<OrderItemData> itemsData = objectMapper.readValue(item.get("items").s(),
                    new TypeReference<List<OrderItemData>>()
                    {
                    });
            List<OrderItem> orderItems = itemsData.stream().map(i -> OrderItem.reconstitute(i.getProductId(),
                    i.getProductName(), i.getQuantity(), i.getUnitPrice())).toList();

            return OrderAggregate.reconstitute(item.get("id").s(), item.get("customerId").s(), orderItems,
                    new BigDecimal(item.get("totalAmount").n()), OrderStatus.valueOf(item.get("status").s()),
                    Instant.parse(item.get("createdAt").s()), Instant.parse(item.get("updatedAt").s()));
        } catch (Exception e)
        {
            throw new IllegalStateException("Failed to map DynamoDB item to OrderAggregate", e);
        }
    }

    Map<String, AttributeValue> toAttributeValues(OrderAggregate order)
    {
        try
        {
            List<OrderItemData> itemsData = order.getItems().stream().map(
                    i -> new OrderItemData(i.productId(), i.productName(), i.quantity(), i.unitPrice().getAmount()))
                    .toList();

            Map<String, AttributeValue> item = new LinkedHashMap<>();
            item.put("id", AttributeValue.fromS(order.getId()));
            item.put("orderDate", AttributeValue
                    .fromS(LocalDate.ofInstant(order.getCreatedAt(), ZoneOffset.UTC).format(DATE_FORMAT)));
            item.put("customerId", AttributeValue.fromS(order.getCustomerId()));
            item.put("totalAmount", AttributeValue.fromN(order.getTotalAmount().getAmount().toPlainString()));
            item.put("currency", AttributeValue.fromS(order.getTotalAmount().getCurrency()));
            item.put("status", AttributeValue.fromS(order.getStatus().name()));
            item.put("createdAt", AttributeValue.fromS(order.getCreatedAt().toString()));
            item.put("updatedAt", AttributeValue.fromS(order.getUpdatedAt().toString()));
            item.put("items", AttributeValue.fromS(objectMapper.writeValueAsString(itemsData)));
            return item;
        } catch (Exception e)
        {
            throw new IllegalStateException("Failed to map OrderAggregate to DynamoDB item", e);
        }
    }
}
