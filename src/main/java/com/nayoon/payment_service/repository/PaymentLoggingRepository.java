package com.nayoon.payment_service.repository;

import com.nayoon.payment_service.entity.PaymentLogging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLoggingRepository extends JpaRepository<PaymentLogging, Long> {

}
