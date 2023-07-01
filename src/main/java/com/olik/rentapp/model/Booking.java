package com.olik.rentapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "pick_up_date_time")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime pick_up_date_time;
	
	@Column(name = "drop_date_time")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime drop_date_time;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    private Product product;
	
	public Booking() {}
	
	public Booking(Product product, LocalDateTime pick_up_date_time, LocalDateTime drop_date_time) {
		this.product = product;
		this.pick_up_date_time = pick_up_date_time;
		this.drop_date_time = drop_date_time;
	}
	
	public long getId() {
		return id;
	}
	
	public LocalDateTime getPick_up_date_time() {
		return pick_up_date_time;
	}
	
	public LocalDateTime getDrop_date_time() {
		return drop_date_time;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setPick_up_date_time(LocalDateTime pick_up_date_time) {
		this.pick_up_date_time = pick_up_date_time;
	}
	
	public void setDrop_date_time(LocalDateTime drop_date_time) {
		this.drop_date_time = drop_date_time;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
