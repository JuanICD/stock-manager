package com.jicd.stockmanager.repository;

import com.jicd.stockmanager.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
