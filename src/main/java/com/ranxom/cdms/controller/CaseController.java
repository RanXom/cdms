package com.ranxom.cdms.controller;

import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.services.CreateCaseService;
import com.ranxom.cdms.services.DeleteCaseService;
import com.ranxom.cdms.services.GetCasesService;
import com.ranxom.cdms.services.UpdateCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CaseController {

    private final CreateCaseService createCaseService;
    private final GetCasesService getCasesService;
    private final UpdateCaseService updateCaseService;
    private final DeleteCaseService deleteCaseService;

    public CaseController(CreateCaseService createCaseService,
                          GetCasesService getCasesService,
                          UpdateCaseService updateCaseService,
                          DeleteCaseService deleteCaseService) {
        this.createCaseService = createCaseService;
        this.getCasesService = getCasesService;
        this.updateCaseService = updateCaseService;
        this.deleteCaseService = deleteCaseService;
    }

    @PostMapping
    public ResponseEntity<String> createCase() {
        return createCaseService.execute(null);
    }

    @GetMapping
    public ResponseEntity<List<Cases>> getCases() {
        return getCasesService.execute(null);
    }

    @PutMapping
    public ResponseEntity<String> updateCase() {
        return updateCaseService.execute(null);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCase() {
        return deleteCaseService.execute(null);
    }
}