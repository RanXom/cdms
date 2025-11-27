package com.ranxom.cdms.repository;

import com.ranxom.cdms.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Cases, Long> {
    List<Cases> findByStatus(String status);
    List<Cases> findByLeadOfficer_OfficersId(Long officerId);
}
