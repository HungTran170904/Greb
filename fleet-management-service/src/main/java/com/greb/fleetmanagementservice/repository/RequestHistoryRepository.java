package com.greb.fleetmanagementservice.repository;

import com.greb.fleetmanagementservice.model.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, String> {
    @Query("select rh from RequestHistory rh where rh.vehicleRequest.id=?1 order by rh.createdAt DESC")
    List<RequestHistory> findByVehicelRequestId(String vehicleRequestId);
}
