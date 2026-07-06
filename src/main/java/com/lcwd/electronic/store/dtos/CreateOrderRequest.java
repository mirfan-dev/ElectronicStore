package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.enums.OrderStatus;
import com.lcwd.electronic.store.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart id is required !!")
    private String cartId;

    @NotBlank(message = "Cart id is required !!")
    private String userId;


    private OrderStatus orderStatus ;
    private PaymentStatus paymentStatus ;
    @NotBlank(message = "Address is required !!")
    private String billingAddress;
    @NotBlank(message = "Phone number is required !!")
    private String billingPhone;
    @NotBlank(message = "Billing name  is required !!")
    private String billingName;


}
