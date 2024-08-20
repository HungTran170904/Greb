package com.greb.fleetmanagementservice.repository;

import com.greb.fleetmanagementservice.model.Vehicle;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    @Query("select v from Vehicle v where " +
            "(:vehicleType is null OR v.vehicleType=:vehicleType) " +
            "AND (:licencePlate='' OR LOWER(v.licencePlate) LIKE %:licencePlate%) " +
            "AND (:manufacturer='' OR LOWER(v.manufacturer) LIKE %:manufacturer%) " +
            "AND (:isAvailable is null OR v.isAvailable=:isAvailable) " +
            "order by v.createdAt DESC")
    Page<Vehicle> searchVehicles(
            @Param("vehicleType") VehicleType vehicleType,
            @Param("licencePlate") String licencePlate,
            @Param("manufacturer") String manufacturer,
            @Param("isAvailable") Boolean isAvailable,
            Pageable pageable);

    List<Vehicle> findByDriverId(String driverId);

    String findLicencePlateById(String vehicleId);
}
