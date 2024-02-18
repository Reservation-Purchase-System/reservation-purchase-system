package com.nayoon.product_service.product.controller.dto.request;

import com.nayoon.product_service.common.exception.CustomException;
import com.nayoon.product_service.common.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ProductCreateRequestDto(
    @NotBlank
    String name,
    @NotBlank
    String content,
    @NotNull
    Long price,
    @NotNull
    Integer stock,
    @NotNull
    Boolean isReserved,
    LocalDateTime openAt
) {

    // isReserved가 true일 때 openAt이 비어있지 않도록 체크
    public ProductCreateRequestDto {
        if (isReserved && openAt == null) {
            throw new CustomException(ErrorCode.RESERVED_PRODUCT_MUST_HAVE_OPEN_AT_FIELD);
        }
    }

}
