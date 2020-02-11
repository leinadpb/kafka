package com.streams.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentEvent {
    private String transactionId;
    private Order order;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
