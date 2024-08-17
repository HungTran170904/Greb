package com.greb.repository;

import com.greb.model.Driver;
import com.greb.model.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String> {
    Driver findByUserId(String userId);

    @Query("select d.id from Driver d where d.userId=?1")
    String findIdByUserId(String userId);

    @Query("select d from Driver d where " +
            "((:name is null OR :name='') OR LOWER(d.name) LIKE %:name% )"+
            "AND ((:jobStatus is null) OR d.jobStatus=:jobStatus) " +
            "order by d.createdAt DESC")
    Page<Driver> searchDrivers(
          @Param("name") String name,
          @Param("jobStatus") JobStatus jobStatus,
          Pageable pageable);
}
