package com.ecobazaarx.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "order_date")
    private java.sql.Timestamp orderDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_carbon_kg")
    private Double totalCarbonKg;

    // Shipping fields
    @Column(name = "shipping_name")
    private String shippingName;

    @Column(name = "shipping_address_1")
    private String shippingAddress1;

    @Column(name = "shipping_address_2")
    private String shippingAddress2;

    @Column(name = "shipping_city")
    private String shippingCity;

    @Column(name = "shipping_state")
    private String shippingState;

    @Column(name = "shipping_postal")
    private String shippingPostal;

    @Column(name = "shipping_country")
    private String shippingCountry;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

    public double getTotalCarbon() {
        return this.totalCarbonKg != null ? this.totalCarbonKg : 0.0;
    }
}
