package com.nayoon.user_service.auth.controller;

import com.nayoon.user_service.auth.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LogoutController {

  private final LogoutService logoutService;

  /**
   * 로그아웃 컨트롤러
   */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      HttpServletRequest request
  ) {
    logoutService.logout(request);
    return ResponseEntity.ok().build();
  }

}
