package com.openmind.order.infrastructure.repositories;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Plain DTO for the JSON blob stored in the DynamoDB "items" string attribute.
 * Keeps PascalCase JSON property names on purpose: docker-compose.yaml's
 * DynamoDB Local seed script writes sample orders with this exact casing, and
 * this DTO must keep reading it.
 */
public class OrderItemData
{
    @JsonProperty("ProductId")
    private String productId;

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("Quantity")
    private int quantity;

    @JsonProperty("UnitPrice")
    private BigDecimal unitPrice;

    public OrderItemData()
    {
    }

    public OrderItemData(String productId, String productName, int quantity, BigDecimal unitPrice)
    {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }
}
