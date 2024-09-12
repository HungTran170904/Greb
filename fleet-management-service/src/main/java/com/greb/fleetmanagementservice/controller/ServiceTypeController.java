package com.greb.fleetmanagementservice.controller;

import com.greb.fleetmanagementservice.dto.ServiceType.ReqServiceTypeDto;
import com.greb.fleetmanagementservice.dto.ServiceType.ResServiceTypeDto;
import com.greb.fleetmanagementservice.service.ServiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/service-types")
    public ResponseEntity<ResServiceTypeDto> createServiceType(
            @RequestBody @Valid ReqServiceTypeDto dto
    ){
        return ResponseEntity.ok(serviceTypeService.createServiceType(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/service-types/{serviceTypeId}")
    public ResponseEntity<ResServiceTypeDto> updateServiceType(
            @PathVariable("serviceTypeId") String serviceTypeId,
            @RequestBody @Valid ReqServiceTypeDto dto
    ){
        return ResponseEntity.ok(serviceTypeService.updateServiceType(serviceTypeId,dto));
    }

    @GetMapping("/service-types/{serviceTypeId}")
    public ResponseEntity<ResServiceTypeDto> getById(
            @PathVariable("serviceTypeId") String serviceTypeId
    ){
        return ResponseEntity.ok(serviceTypeService.getById(serviceTypeId));
    }

    @GetMapping("/service-types")
    public ResponseEntity<List<ResServiceTypeDto>> getAll(){
        return ResponseEntity.ok(serviceTypeService.getAll());
    }
}
