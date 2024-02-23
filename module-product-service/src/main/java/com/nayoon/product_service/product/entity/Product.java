package com.nayoon.product_service.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id", updatable = false)
  private Long id;

  @Column(name = "user_id", nullable = false) // 상품 등록한 사용자 ID
  private Long userId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "is_reserved", nullable = false)
  private Boolean isReserved;

  @Column(name = "open_at")
  private LocalDateTime openAt;

  @Column(name = "created_at", nullable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  public Product(Long userId, String name, String content, Long price, Boolean isReserved,
      LocalDateTime openAt, LocalDateTime deletedAt) {
    this.userId = userId;
    this.name = name;
    this.content = content;
    this.price = price;
    this.isReserved = isReserved;
    this.openAt = openAt;
    this.deletedAt = deletedAt;
  }

  public void update(String name, String content, Long price, Boolean isReserved, LocalDateTime openAt) {
    this.name = name;
    this.content = content;
    this.price = price;
    this.isReserved = isReserved;
    this.openAt = openAt;
  }

}
