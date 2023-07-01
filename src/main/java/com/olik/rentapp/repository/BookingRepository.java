package com.olik.rentapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olik.rentapp.model.Booking;
import com.olik.rentapp.model.Product;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByProduct(Product product);
}
