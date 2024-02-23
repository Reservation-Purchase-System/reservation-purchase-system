package com.nayoon.product_service.reservation_product.repository;

import com.nayoon.product_service.reservation_product.entity.ReservationProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationProductQRepository {

  Page<ReservationProduct> filterAllReservationProducts(Pageable pageable);

}
