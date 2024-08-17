package com.greb.controller;

import com.greb.dto.Customer.ListCustomersDto;
import com.greb.dto.Customer.RegisterCustomerDto;
import com.greb.dto.Customer.ResponseCustomerDto;
import com.greb.dto.Customer.UpdateCustomerDto;
import com.greb.dto.ErrorDto;
import com.greb.service.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = ResponseCustomerDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseCustomerDto> register(
            @Valid @RequestBody RegisterCustomerDto dto
    ){
        return ResponseEntity.ok(customerService.register(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "204", description = "Updated")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/update-profile")
    public ResponseEntity<Void> updateProfile(
            @Valid @RequestBody UpdateCustomerDto dto
    ){
        customerService.updateProfile(dto);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/private/update-discountpoint")
    public ResponseEntity<Void> updateDiscountPoint(
            @RequestParam("discountPoint") Integer discountPoint
    ){
        customerService.updateDiscountPoint(discountPoint);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = ResponseCustomerDto.class)))
    })
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/profile")
    public ResponseEntity<ResponseCustomerDto>  getProfile(){
        return ResponseEntity.ok(customerService.getProfile());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ListCustomersDto> searchCustomers(
            @RequestParam(value="name",defaultValue = "", required = false) String name,
            @RequestParam(value="pageNo") @Min(0) Integer pageNo,
            @RequestParam("pageSize") @Min(1) Integer pageSize
    ){
        return ResponseEntity.ok(customerService.searchCustomers(name, pageNo, pageSize));
    }
}
