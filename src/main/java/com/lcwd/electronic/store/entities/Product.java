package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {


    @Id
    private String productId;
    private String title;
    @Column(length = 10000)
    private String description;
    private int price;
    private int discountPercentage;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private  Category category;


    @Transient
    public int getDiscountedPrice() {

        if (discountPercentage < 0 || discountPercentage > 100) {
            return price;
        }

        return price - (price * discountPercentage / 100);
    }


}
