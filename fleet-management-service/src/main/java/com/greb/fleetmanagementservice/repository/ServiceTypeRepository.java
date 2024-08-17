package com.greb.fleetmanagementservice.repository;

import com.greb.fleetmanagementservice.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {
    public Boolean existsByName(String name);
}
