package com.jicd.stockmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "sale_detail")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private Long id;

    @Column(name = "sale_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sale_unit_price", nullable = false,precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "sale_total_price",precision = 10, scale = 2)
    private BigDecimal totalPrice;

    //Relacion ManyToOne con Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    //Relacion ManyToOne con Sales
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id",nullable = false)
    private Sale sale;


}
