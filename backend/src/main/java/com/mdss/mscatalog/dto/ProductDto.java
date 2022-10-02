package com.mdss.mscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.mdss.mscatalog.entities.Category;
import com.mdss.mscatalog.entities.Product;

public class ProductDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@Size(min = 5, max = 60, message = "Name should be between 5 and 60 characteres!")
	@NotBlank(message = "Required field!")
	private String name;
	@NotBlank(message = "Requred field!")
	private String description;
	@Positive(message = "The price should be greater then zero!")
	private Double price;
	private String imgUrl;	
	@PastOrPresent(message = "The registration date may not be future!")
	private Instant date;
	
	List<CategoryDto> categories = new ArrayList<>();
	
	public ProductDto() {
		
	}

	public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDto(Product entity) {
		id = entity.getId();
		name = entity.getName();
		description = entity.getDescription();
		price = entity.getPrice();
		imgUrl = entity.getImgUrl();
		date = entity.getDate();
	}
	
	public ProductDto(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(x -> this.categories.add(new CategoryDto(x) ));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDto> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDto> categories) {
		this.categories = categories;
	}
	
}
