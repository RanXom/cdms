package com.ranxom.cdms.repository;

import com.ranxom.cdms.model.CaseParticipant;
import com.ranxom.cdms.model.CaseParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseParticipantRepository extends JpaRepository<CaseParticipant, CaseParticipantId> {
    List<CaseParticipant> findByCaseId(Long caseId);
    List<CaseParticipant> findByPersonId(Long personId);
}
