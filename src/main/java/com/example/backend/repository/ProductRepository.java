package com.example.backend.repository;

import com.example.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface extends JpaRepository, providing CRUD operations for the Product entity.
// JpaRepository<Product, Long> means it's for the Product entity and its primary key type is Long.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA automatically provides methods like findAll(), findById(), save(), deleteById(), etc.
    // No custom methods are needed for a basic "get all products" functionality.
}
