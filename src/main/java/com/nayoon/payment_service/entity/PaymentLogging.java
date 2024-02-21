package com.nayoon.payment_service.entity;

import com.nayoon.payment_service.type.PaymentAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "payment_logging")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PaymentLogging {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id", updatable = false)
  private Long id;

  @Column(name = "purchase_id", nullable = false)
  private Long purchaseId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "action", nullable = false)
  @Enumerated(EnumType.STRING)
  private PaymentAction action;

  @Column(name = "timestamp", nullable = false)
  @CreatedDate
  private LocalDateTime timestamp;

  public PaymentLogging(Payment payment, PaymentAction action) {
    this.purchaseId = payment.getPurchaseId();
    this.userId = payment.getUserId();
    this.action = action;
  }

}
