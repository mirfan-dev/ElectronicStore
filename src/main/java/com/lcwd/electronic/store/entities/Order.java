package com.lcwd.electronic.store.entities;

import com.lcwd.electronic.store.enums.OrderStatus;
import com.lcwd.electronic.store.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //PENDING,DISPATCHED,DELIVERED,
    //enum
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    //NOT-PAID, PAID
    //enum
    //boolean- false=>NOTPAID  || true=>PAID
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus=PaymentStatus.NOTPAID;

    private int orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


}
