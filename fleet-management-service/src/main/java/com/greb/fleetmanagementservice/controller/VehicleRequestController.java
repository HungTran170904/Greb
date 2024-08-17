package com.greb.fleetmanagementservice.controller;

import com.greb.fleetmanagementservice.dto.RequestHistory.RequestHistoryDto;
import com.greb.fleetmanagementservice.dto.VehicleRequest.*;
import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.RequestType;
import com.greb.fleetmanagementservice.service.VehicleRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle-request")
@RequiredArgsConstructor
public class VehicleRequestController {
    private final VehicleRequestService vehicleRequestService;

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/create")
    public ResponseEntity<ResVehicleRequestDto> create(
            @RequestBody @Valid CreateVehicleRequestDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.create(dto));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/update/{vehicleRequestId}")
    public ResponseEntity<ResVehicleRequestDto> update(
            @PathVariable("vehicleRequestId") String vehicleRequestId,
            @RequestBody @Valid UpdateVehicleRequestDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.update(vehicleRequestId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve-request")
    public ResponseEntity<RequestHistoryDto> approveRequest(
            @RequestBody @Valid ApprovalDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.approveRequest(dto));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @DeleteMapping("cancel-request/{vehicleRequestId}")
    public ResponseEntity<Void> cancelRequest(
            @PathVariable("vehicleRequestId") String vehicleRequestId
    ){
        vehicleRequestService.cancelRequest(vehicleRequestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/request-history/{vehicleRequestId}")
    public ResponseEntity<List<RequestHistoryDto>> getRequestHistoriesByVehicleRequestId(
            @PathVariable("vehicleRequestId") String vehicleRequestId
    ){
        return ResponseEntity.ok(vehicleRequestService.getRequestHistoriesByVehicleRequestId(vehicleRequestId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ListVehicleRequestsDto> searchVehicleRequests(
            @RequestParam(value = "requestStatus", required = false) RequestStatus status,
            @RequestParam(value="requestType", required = false) RequestType requestType,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(vehicleRequestService.searchVehicleRequests(
                status,
                requestType,
                pageNo,
                pageSize
        ));
    }

    @GetMapping("/driver")
    public ResponseEntity<ListVehicleRequestsDto> getRequestsByDriverId(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(vehicleRequestService.getRequestsByDriverId(pageNo, pageSize));
    }
}
