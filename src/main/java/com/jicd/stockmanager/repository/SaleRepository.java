package com.jicd.stockmanager.repository;

import com.jicd.stockmanager.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
