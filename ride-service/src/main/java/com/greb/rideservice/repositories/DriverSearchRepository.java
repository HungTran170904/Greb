package com.greb.rideservice.repositories;

import com.greb.rideservice.models.DriverSearch;
import com.greb.rideservice.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverSearchRepository extends JpaRepository<DriverSearch, String> {
    DriverSearch findByRide(Ride ride);
}
