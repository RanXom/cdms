package com.ranxom.cdms.services;

import com.ranxom.cdms.core.Command;
import com.ranxom.cdms.dto.CaseDTO;
import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.model.Officer;
import com.ranxom.cdms.repository.CaseRepository;
import com.ranxom.cdms.repository.OfficerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateCaseService implements Command<CaseDTO, Cases> {

    private final CaseRepository caseRepository;
    private final OfficerRepository officerRepository;

    public CreateCaseService(CaseRepository caseRepository, OfficerRepository officerRepository) {
        this.caseRepository = caseRepository;
        this.officerRepository = officerRepository;
    }

    @Override
    public ResponseEntity<Cases> execute(CaseDTO input) {
        Cases cases = new Cases();
        cases.setTitle(input.getTitle());
        cases.setDescription(input.getDescription());
        cases.setCaseType(input.getCaseType());
        cases.setStatus(input.getStatus() != null ? input.getStatus() : "open");
        cases.setReportedAt(input.getReportedAt());
        cases.setLocation(input.getLocation());
        cases.setSeverity(input.getSeverity());

        if (input.getLeadOfficerId() != null) {
            Officer officer = officerRepository.findById(input.getLeadOfficerId()).orElse(null);
            cases.setLeadOfficer(officer);
        }

        Cases savedCase = caseRepository.save(cases);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCase);
    }
}
