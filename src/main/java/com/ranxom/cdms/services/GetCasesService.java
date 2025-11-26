package com.ranxom.cdms.services;

import com.ranxom.cdms.core.Query;
import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.repository.CaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCasesService implements Query<Void, List<Cases>> {

    private final CaseRepository caseRepository;

    public GetCasesService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public ResponseEntity<List<Cases>> execute(Void input) {
        List<Cases> cases = caseRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(cases);
    }

}
