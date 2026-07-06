package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.enums.OrderStatus;
import com.lcwd.electronic.store.enums.PaymentStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {

    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    private Date deliveredDate;


}
