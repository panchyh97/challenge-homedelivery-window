package com.challenge.homedelivery.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_window_id")
    private DeliveryWindow deliveryWindow;

    @Column(name = "order_reference", nullable = false, unique = true)
    private String orderReference;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Reservation() {}

    public Reservation(DeliveryWindow deliveryWindow, String orderReference, String address, LocalDateTime createdAt) {
        this.deliveryWindow = deliveryWindow;
        this.orderReference = orderReference;
        this.address = address;
        this.status = ReservationStatus.CONFIRMED;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public DeliveryWindow getDeliveryWindow() { return deliveryWindow; }
    public String getOrderReference() { return orderReference; }
    public String getAddress() { return address; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}