package com.mdss.mscatalog.tests;

import java.time.Instant;

import com.mdss.mscatalog.dto.ProductDto;
import com.mdss.mscatalog.entities.Category;
import com.mdss.mscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L,"Phone", Instant.parse("2020-07-14T10:00:00Z") , "Good Tv",2190.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg");
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDto createProductDto() {
		Product product = createProduct();
		return new ProductDto(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}
}
