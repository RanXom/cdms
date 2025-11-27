package com.ranxom.cdms.services;

import com.ranxom.cdms.core.Command;
import com.ranxom.cdms.exceptions.ResourceNotFoundException;
import com.ranxom.cdms.repository.CaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteCaseService implements Command<Long, String> {

    private final CaseRepository caseRepository;

    public DeleteCaseService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public ResponseEntity<String> execute(Long caseId) {
        if (!caseRepository.existsById(caseId)) {
            throw new ResourceNotFoundException("Case", "id", caseId);
        }

        caseRepository.deleteById(caseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Case deleted successfully");
    }
}
