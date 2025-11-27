package com.ranxom.cdms.repository;

import com.ranxom.cdms.model.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    Optional<Officer> findByBadgeNumber(String badgeNumber);
    boolean existsByBadgeNumber(String badgeNumber);
}
