package com.greb.rideservice.repositories;

import com.greb.rideservice.models.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, String> {
    @Query("select r from Ride r where r.customerId=?1 order by r.createdAt DESC")
    Page<Ride> getRidesByCustomerId(String customerId, Pageable pageable);

    @Query("select r from Ride r where r.driverId=?1 order by r.createdAt DESC")
    Page<Ride> getRidesByDriverId(String driverId, Pageable pageable);
}
