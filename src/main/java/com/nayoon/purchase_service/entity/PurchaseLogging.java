package com.nayoon.purchase_service.entity;

import com.nayoon.purchase_service.type.PurchaseAction;
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

@Table(name = "purchase_logging")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PurchaseLogging {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "purchase_id", nullable = false)
  private Long purchaseId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "action", nullable = false)
  @Enumerated(EnumType.STRING)
  private PurchaseAction action;

  @Column(name = "timestamp", nullable = false)
  @CreatedDate
  private LocalDateTime timestamp;

  public PurchaseLogging(Purchase purchase, PurchaseAction action) {
    this.purchaseId = purchase.getId();
    this.userId = purchase.getUserId();
    this.productId = purchase.getProductId();
    this.quantity = purchase.getQuantity();
    this.address = purchase.getAddress();
    this.action = action;
  }

}
