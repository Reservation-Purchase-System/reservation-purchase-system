package com.nayoon.purchase_service.repository;

import com.nayoon.purchase_service.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseQRepository {

}
