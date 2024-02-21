package com.nayoon.purchase_service.client.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductResponseDto(
    String name,
    String content,
    Long price,
    Boolean isReserved,
    LocalDateTime openAt
) {

}
