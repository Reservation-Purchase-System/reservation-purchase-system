package com.nayoon.product_service.reservation_product.repository;

import com.nayoon.product_service.reservation_product.entity.ReservationProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationProductStockRepository extends JpaRepository<ReservationProductStock, Long> {

}
