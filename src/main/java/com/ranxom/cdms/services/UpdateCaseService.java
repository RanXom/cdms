package com.ranxom.cdms.services;

import com.ranxom.cdms.core.Command;
import com.ranxom.cdms.dto.CaseDTO;
import com.ranxom.cdms.exceptions.ResourceNotFoundException;
import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.model.Officer;
import com.ranxom.cdms.repository.CaseRepository;
import com.ranxom.cdms.repository.OfficerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateCaseService implements Command<CaseDTO, Cases> {

    private final CaseRepository caseRepository;
    private final OfficerRepository officerRepository;

    public UpdateCaseService(CaseRepository caseRepository, OfficerRepository officerRepository) {
        this.caseRepository = caseRepository;
        this.officerRepository = officerRepository;
    }

    @Override
    public ResponseEntity<Cases> execute(CaseDTO input) {
        Cases cases = caseRepository.findById(input.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case", "id", input.getCaseId()));

        if (input.getTitle() != null) cases.setTitle(input.getTitle());
        if (input.getDescription() != null) cases.setDescription(input.getDescription());
        if (input.getCaseType() != null) cases.setCaseType(input.getCaseType());
        if (input.getStatus() != null) cases.setStatus(input.getStatus());
        if (input.getReportedAt() != null) cases.setReportedAt(input.getReportedAt());
        if (input.getLocation() != null) cases.setLocation(input.getLocation());
        if (input.getSeverity() != null) cases.setSeverity(input.getSeverity());

        if (input.getLeadOfficerId() != null) {
            Officer officer = officerRepository.findById(input.getLeadOfficerId()).orElse(null);
            cases.setLeadOfficer(officer);
        }

        Cases updatedCase = caseRepository.save(cases);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCase);
    }
}
