package com.mdss.mscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdss.mscatalog.dto.CategoryDto;
import com.mdss.mscatalog.dto.ProductDto;
import com.mdss.mscatalog.entities.Category;
import com.mdss.mscatalog.entities.Product;
import com.mdss.mscatalog.repositories.CategoryRepository;
import com.mdss.mscatalog.repositories.ProductRepository;
import com.mdss.mscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDto> findAllPaged(Long categoryId, String name, Pageable pageable){
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> page = repository.search(categories, name, pageable);
		repository.findProductWithCategories(page.getContent());
		return page.map(x-> new ProductDto(x, x.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDto findById(Long Id) {
		Optional<Product> obj = repository.findById(Id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		return new ProductDto(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDto insert(ProductDto dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		
		entity = repository.save(entity);
		return new ProductDto(entity);
	}
	
	@Transactional
	public ProductDto update(Long id, ProductDto dto) {
		try{Product entity = repository.getOne(id);
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDto(entity);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}
	}

	public void delete(Long id) {
		try {repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}catch(DataIntegrityViolationException e) {
			
		}
	}
	
	private void copyDtoToEntity(ProductDto dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for(CategoryDto catDto: dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}
