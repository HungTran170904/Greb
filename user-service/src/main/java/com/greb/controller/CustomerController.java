package com.greb.controller;

import com.greb.dto.Customer.RegisterCustomerDto;
import com.greb.dto.Customer.ResponseCustomerDto;
import com.greb.dto.Customer.UpdateCustomerDto;
import com.greb.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public ResponseEntity<ResponseCustomerDto> register(
            @Valid @RequestBody RegisterCustomerDto dto
    ){
        return ResponseEntity.ok(customerService.register(dto));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/update-profile")
    public ResponseEntity<Void> updateProfile(
            @Valid @RequestBody UpdateCustomerDto dto
    ){
        customerService.updateProfile(dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/private/update-discountpoint")
    public ResponseEntity<Void> updateDiscountPoint(
            @RequestParam("discountPoint") Integer discountPoint
    ){
        customerService.updateDiscountPoint(discountPoint);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/profile")
    public ResponseEntity<ResponseCustomerDto>  getProfile(){
        return ResponseEntity.ok(customerService.getProfile());
    }
}
