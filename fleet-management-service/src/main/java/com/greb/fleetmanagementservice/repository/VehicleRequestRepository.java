package com.greb.fleetmanagementservice.repository;

import com.greb.fleetmanagementservice.model.VehicleRequest;
import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRequestRepository extends JpaRepository<VehicleRequest, String> {
    @Query("select vr from VehicleRequest vr where " +
            "(:status is null OR vr.status=:status) " +
            "AND (:requestType is null OR vr.requestType=:requestType) "+
            "order by vr.createdAt")
    Page<VehicleRequest> searchVehicleRequests(
            @Param("status") RequestStatus requestStatus,
            @Param("requestType") RequestType requestType,
            Pageable pageable);

    @Query("select vr from VehicleRequest vr where vr.driverId=:driverId order by vr.createdAt")
    Page<VehicleRequest> getByDriverId(@Param("driverId") String driverId, Pageable pageable);
}
