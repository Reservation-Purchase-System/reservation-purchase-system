package com.nayoon.product_service.product.controller.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductUpdateRequestDto(
    String name,
    String content,
    Long price,
    Integer stock,
    Boolean isReserved,
    LocalDateTime openAt
) {

}
