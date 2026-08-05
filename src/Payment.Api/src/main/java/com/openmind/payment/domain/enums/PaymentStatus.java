package com.openmind.payment.domain.enums;

/**
 * Constant names kept in the original PascalCase (not Java's UPPER_SNAKE_CASE) for
 * consistency with how these are stored as DynamoDB attribute value strings.
 */
public enum PaymentStatus {
    Pending, Processed, Failed
}
