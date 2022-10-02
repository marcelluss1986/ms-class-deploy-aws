package com.mdss.mscatalog.services;

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
import com.mdss.mscatalog.entities.Category;
import com.mdss.mscatalog.repositories.CategoryRepository;
import com.mdss.mscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDto> findAllPaged(Pageable pageable){
		Page<Category> list = repository.findAll(pageable);
		return list.map(x-> new CategoryDto(x));
	}
	
	@Transactional(readOnly = true)
	public CategoryDto findById(Long Id) {
		Optional<Category> obj = repository.findById(Id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		return new CategoryDto(entity);
	}
	
	@Transactional
	public CategoryDto insert(CategoryDto dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDto(entity);
	}
	
	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		try{Category entity = repository.getOne(id);
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDto(entity);
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
}
