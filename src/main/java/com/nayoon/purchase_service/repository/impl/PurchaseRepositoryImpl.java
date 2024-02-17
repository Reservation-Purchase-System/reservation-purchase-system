package com.nayoon.purchase_service.repository.impl;

import static com.nayoon.purchase_service.entity.QPurchase.purchase;

import com.nayoon.purchase_service.entity.Purchase;
import com.nayoon.purchase_service.repository.PurchaseQRepository;
import com.nayoon.purchase_service.type.PurchaseStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PurchaseRepositoryImpl implements PurchaseQRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Purchase> getOrdersByUserId(Long userId, Pageable pageable) {
    JPAQuery<Purchase> query = queryFactory
        .selectFrom(purchase)
        .where(purchase.userId.eq(userId)
            .and(purchase.purchaseStatus.eq(PurchaseStatus.CONFIRMED)));

    List<OrderSpecifier> order = new ArrayList<>();
    pageable.getSort().stream().forEach(o -> {
      order.add(new OrderSpecifier(
          o.getDirection().isDescending() ? Order.DESC : Order.ASC,
          new PathBuilder(Purchase.class, "purchase").get(o.getProperty())));
    });

    query = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(order.toArray(new OrderSpecifier[0]));

    List<Purchase> result = query.fetch();
    return new PageImpl<>(result, pageable, result.size());
  }

}
