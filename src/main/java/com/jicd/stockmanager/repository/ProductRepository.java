package com.jicd.stockmanager.repository;

import com.jicd.stockmanager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //Productos con stock bajo
    List<Product> findByStockQuantityLessThan(Integer minThreshold);

    List<Product> findByNameContainingIgnoreCase(String name);

}
