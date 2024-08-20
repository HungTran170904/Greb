package com.greb.userservice.repository;

import com.greb.userservice.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("update Customer c set c.discountPoint=?2 where c.userId=?1")
    void updateDiscountPoint(String userId, Integer discountPoint);

    @Query("select c from Customer c where " +
            "((:name is null OR :name='') OR LOWER(c.name) LIKE %:name% ) "+
            "order by c.createdAt")
    Page<Customer> searchCustomers(@Param("name") String name, Pageable pageable);
}
