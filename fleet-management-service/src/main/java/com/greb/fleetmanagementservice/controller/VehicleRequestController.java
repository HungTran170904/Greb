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
@RequiredArgsConstructor
public class VehicleRequestController {
    private final VehicleRequestService vehicleRequestService;

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/vehicle-requests")
    public ResponseEntity<ResVehicleRequestDto> create(
            @RequestBody @Valid CreateVehicleRequestDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.create(dto));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/vehicle-requests/{vehicleRequestId}")
    public ResponseEntity<ResVehicleRequestDto> update(
            @PathVariable("vehicleRequestId") String vehicleRequestId,
            @RequestBody @Valid UpdateVehicleRequestDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.update(vehicleRequestId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/vehicle-requests/approving")
    public ResponseEntity<RequestHistoryDto> approveRequest(
            @RequestBody @Valid ApprovalDto dto
    ){
        return ResponseEntity.ok(vehicleRequestService.approveRequest(dto));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @DeleteMapping("/vehicle-requests/cancelling/{vehicleRequestId}")
    public ResponseEntity<Void> cancelRequest(
            @PathVariable("vehicleRequestId") String vehicleRequestId
    ){
        vehicleRequestService.cancelRequest(vehicleRequestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-requests/{vehicleRequestId}/request-histories")
    public ResponseEntity<List<RequestHistoryDto>> getRequestHistoriesById(
            @PathVariable("vehicleRequestId") String vehicleRequestId
    ){
        return ResponseEntity.ok(vehicleRequestService.getRequestHistoriesById(vehicleRequestId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/vehicle-requests")
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

    @GetMapping("/drivers/vehicle-requests")
    public ResponseEntity<ListVehicleRequestsDto> getRequestsByDriverId(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(vehicleRequestService.getRequestsByDriverId(pageNo, pageSize));
    }
}
