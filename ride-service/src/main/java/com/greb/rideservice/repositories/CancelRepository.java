package com.greb.rideservice.repositories;

import com.greb.rideservice.models.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRepository extends JpaRepository<Cancel, String> {
}
