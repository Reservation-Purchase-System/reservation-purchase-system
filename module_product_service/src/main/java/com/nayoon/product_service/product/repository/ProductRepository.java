package com.nayoon.product_service.product.repository;

import com.nayoon.product_service.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductQRepository {

}
