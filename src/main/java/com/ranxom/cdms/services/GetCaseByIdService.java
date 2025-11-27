package com.ranxom.cdms.services;

import com.ranxom.cdms.core.Query;
import com.ranxom.cdms.exceptions.ResourceNotFoundException;
import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.repository.CaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetCaseByIdService implements Query<Long, Cases> {

    private final CaseRepository caseRepository;

    public GetCaseByIdService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public ResponseEntity<Cases> execute(Long caseId) {
        Cases cases = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case", "id", caseId));

        return ResponseEntity.status(HttpStatus.OK).body(cases);
    }
}
