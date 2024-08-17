package com.greb.controller;

import com.greb.dto.User.ResponseUserDto;
import com.greb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
   private final UserService userService;

   @PreAuthorize("hasRole('ADMIN')")
   @GetMapping("/profile/{userId}")
   public ResponseEntity<ResponseUserDto> getProfile(
           @PathVariable("userId") String userId
   ){
       return ResponseEntity.ok(userService.getProfile(userId));
   }
}
