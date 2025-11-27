package com.ranxom.cdms.repository;

import com.ranxom.cdms.model.InvestigationInstance;
import com.ranxom.cdms.model.InvestigationInstanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestigationInstanceRepository extends JpaRepository<InvestigationInstance, InvestigationInstanceId> {
    List<InvestigationInstance> findByCaseId(Long caseId);
}
