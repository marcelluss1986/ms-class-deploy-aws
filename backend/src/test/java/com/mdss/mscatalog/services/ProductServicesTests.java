package com.mdss.mscatalog.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mdss.mscatalog.dto.ProductDto;
import com.mdss.mscatalog.entities.Category;
import com.mdss.mscatalog.entities.Product;
import com.mdss.mscatalog.repositories.CategoryRepository;
import com.mdss.mscatalog.repositories.ProductRepository;
import com.mdss.mscatalog.services.exceptions.DatabaseException;
import com.mdss.mscatalog.services.exceptions.ResourceNotFoundException;
import com.mdss.mscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServicesTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long notExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	
	@BeforeEach
	void setUp()throws Exception{
		existingId = 1L;
		notExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)any())).thenReturn(page);
		Mockito.when(repository.save(any())).thenReturn(product);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(notExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.search(any(), any(), any())).thenReturn(page);
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(notExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(notExistingId)).thenThrow(ResourceNotFoundException.class);
		
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(notExistingId);
		Mockito.doThrow(DatabaseException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			ProductDto productDto = Factory.createProductDto();
			service.update(notExistingId, productDto);
		});
	}
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() {
		ProductDto productDto = Factory.createProductDto();
		ProductDto result = service.update(existingId, productDto);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(notExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		ProductDto dto = service.findById(existingId);
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
			});
		Mockito.verify(repository,Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDto> result = service.findAllPaged(0L, "", pageable);
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});
		
		Mockito.verify(repository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(notExistingId);
		});
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(notExistingId);
	}
	
	
	
}
