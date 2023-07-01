package com.olik.rentapp.model;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "image")
	private String image;
	
	@Column(name="cost_per_hour")
	private Integer cost_per_hour;
	
	@Column(name="rating")
	private float rating;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category category;
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImage() {
		return image;
	}
	
	public Integer getCost_per_hour() {
		return cost_per_hour;
	}
	
	public float getRating() {
		return rating;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public void setCost_per_hour(Integer cost_per_hour) {
		this.cost_per_hour = cost_per_hour;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
}
