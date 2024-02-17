package com.nayoon.purchase_service.repository;

import com.nayoon.purchase_service.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseQRepository {

  Page<Purchase> getOrdersByUserId(Long userId, Pageable pageable);

}
