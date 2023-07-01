package com.olik.rentapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olik.rentapp.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
