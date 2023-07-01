package com.olik.rentapp.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olik.rentapp.model.Booking;
import com.olik.rentapp.model.Category;
import com.olik.rentapp.model.Product;
import com.olik.rentapp.repository.ProductRepository;
import com.olik.rentapp.repository.BookingRepository;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	BookingRepository bookingRepository;
	
	@GetMapping("/")
	public ResponseEntity<String> checkApi() {
		return new ResponseEntity<>("Tanmay's Server Works", HttpStatus.OK);
	}
	
	private boolean isProductAvailable(Product p, LocalDateTime pick_up_time, LocalDateTime drop_time) {
		List<Booking> bookings = bookingRepository.findByProduct(p);
		
		for(Booking b : bookings) {
			if(! pick_up_time.isBefore(b.getDrop_date_time()) || ! drop_time.isAfter(b.getPick_up_date_time())) {
				// does not clash with this booking
			}
			else {
				// clashes with the booking
				return false;
			}
		}
		
		return true;
	}
	
	
//	assignment API - 1
//	- Returns a list of all the available products with product name, image, cost for the duration etc.
//	- if a product is already booked and booking dates overlaps with search duration, don't show that product in the list.
//	
//	GET API -> takes product id, pick_up_time and drop_time as URL parameters
//	
//	pick up and drop time formats: string - "yyyy-mm-dd hh:mm" (24 hour time format)
	
	@GetMapping("/products/{category}/{pick_up_time}/{drop_time}")
	public ResponseEntity<List<Object>> getProducts(@PathVariable("category") Category c_id, @PathVariable("pick_up_time") String pick_up_time, @PathVariable("drop_time") String drop_time) {
		try {
			List<Product> allProducts = productRepository.findByCategory(c_id);
			
			List<Object> availableProducts = new ArrayList<Object>();
			
			// convert given date times from string to LocalDateTime
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime pick_up = LocalDateTime.parse(pick_up_time, formatter);
			LocalDateTime drop= LocalDateTime.parse(drop_time, formatter);
			
			for(Product p : allProducts) {
				if(isProductAvailable(p, pick_up, drop)) {
					Map<String, String> product = new HashMap<>();
					
					product.put("id", Long.toString(p.getId()));
					product.put("name", p.getName());
					product.put("image", p.getImage());
					product.put("cost_per_hour", Integer.toString( p.getCost_per_hour() ));
					product.put("total_cost", Long.toString( p.getCost_per_hour() * Duration.between(pick_up, drop).toHours()));
					product.put("rating", Float.toString(p.getRating()));
					
					availableProducts.add(product);
				}
	 		}
			return new ResponseEntity<>(availableProducts, HttpStatus.OK);
		} catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	Assignment API - 2
//	- Creates a rental booking: Create a user rental booking for a specific product and duration.
//
//	POST API -> Takes product id, pick up time and drop time as a Json in Request body
//	Ex:
//	{
//		"product": "1",
//		"pick_up_date_time": "2023-06-13 06:00",
//		"drop_date_time": "2023-06-13 12:00"
//	}
	
	@PostMapping("/booking")
	public ResponseEntity<String> createBooking(@RequestBody Map<String, String> json) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime pick_up = LocalDateTime.parse(json.get("pick_up_date_time"), formatter);
		LocalDateTime drop= LocalDateTime.parse(json.get("drop_date_time"), formatter);
		
		try {
			Product product = productRepository.getById(Long.parseLong(json.get("product")));
			if(isProductAvailable(product, pick_up, drop)) {
				Booking _booking = bookingRepository.save(new Booking(product, pick_up, drop));
				return new ResponseEntity<>("Booking created successfully!", HttpStatus.CREATED);
			}
			else {
				// selected date time clashes with a booking for the product
				return new ResponseEntity<>("Sorry, Booking clashes with an another booking.", HttpStatus.CONFLICT);
			}
			
		} catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Please check your date-time formats.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
