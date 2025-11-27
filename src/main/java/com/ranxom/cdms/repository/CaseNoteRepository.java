package com.ranxom.cdms.repository;

import com.ranxom.cdms.model.CaseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseNoteRepository extends JpaRepository<CaseNote, Long> {
    List<CaseNote> findByCases_CaseId(Long caseId);
    List<CaseNote> findByCases_CaseIdOrderByCreatedAtDesc(Long caseId);
}
