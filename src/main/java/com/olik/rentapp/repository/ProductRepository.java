package com.olik.rentapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olik.rentapp.model.Category;
import com.olik.rentapp.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategory(Category category);
}
