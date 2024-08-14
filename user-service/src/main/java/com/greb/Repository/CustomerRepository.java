package com.greb.Repository;

import com.greb.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    public Customer findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("update Customer c set c.discountPoint=?2 where c.userId=?1")
    public void updateDiscountPoint(String userId, Integer discountPoint);
}
