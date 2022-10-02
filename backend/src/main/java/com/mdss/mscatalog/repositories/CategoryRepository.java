package com.mdss.mscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdss.mscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
