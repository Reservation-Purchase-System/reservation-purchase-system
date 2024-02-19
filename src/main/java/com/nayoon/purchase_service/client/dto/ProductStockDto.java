package com.nayoon.purchase_service.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record ProductStockDto(
    Long productId,
    Integer stock,
    LocalDateTime openAt
) {

}
