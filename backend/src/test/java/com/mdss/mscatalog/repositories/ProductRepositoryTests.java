package com.mdss.mscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.mdss.mscatalog.entities.Product;
import com.mdss.mscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long notExistsId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		notExistsId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product);
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		Optional<Product> result =  repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(notExistsId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNotEmptyProductWhenIdExists() {
		
		Optional<Product> res = repository.findById(existingId);
		Assertions.assertTrue(res.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyProductWhenIdNotExists() {
		Optional<Product> res = repository.findById(notExistsId);
		Assertions.assertTrue(res.isEmpty());
	}
}
