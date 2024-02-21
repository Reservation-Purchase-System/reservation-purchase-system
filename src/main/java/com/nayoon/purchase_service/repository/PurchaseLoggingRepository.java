package com.nayoon.purchase_service.repository;

import com.nayoon.purchase_service.entity.PurchaseLogging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLoggingRepository extends JpaRepository<PurchaseLogging, Long> {

}
