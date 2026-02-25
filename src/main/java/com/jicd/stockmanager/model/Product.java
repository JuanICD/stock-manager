package com.jicd.stockmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_price", nullable = false)
    private Double price;

    @Column(name = "product_stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "product_min_threshold")
    private Integer minThreshold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<SaleDetail> saleDetails;

}
