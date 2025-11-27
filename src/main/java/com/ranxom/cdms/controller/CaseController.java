package com.ranxom.cdms.controller;

import com.ranxom.cdms.dto.CaseDTO;
import com.ranxom.cdms.model.Cases;
import com.ranxom.cdms.services.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CreateCaseService createCaseService;
    private final GetCasesService getCasesService;
    private final GetCaseByIdService getCaseByIdService;
    private final UpdateCaseService updateCaseService;
    private final DeleteCaseService deleteCaseService;

    public CaseController(CreateCaseService createCaseService,
                          GetCasesService getCasesService,
                          GetCaseByIdService getCaseByIdService,
                          UpdateCaseService updateCaseService,
                          DeleteCaseService deleteCaseService) {
        this.createCaseService = createCaseService;
        this.getCasesService = getCasesService;
        this.getCaseByIdService = getCaseByIdService;
        this.updateCaseService = updateCaseService;
        this.deleteCaseService = deleteCaseService;
    }

    @PostMapping
    public ResponseEntity<Cases> createCase(@Valid @RequestBody CaseDTO caseDTO) {
        return createCaseService.execute(caseDTO);
    }

    @GetMapping
    public ResponseEntity<List<Cases>> getAllCases() {
        return getCasesService.execute(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cases> getCaseById(@PathVariable Long id) {
        return getCaseByIdService.execute(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cases> updateCase(@PathVariable Long id, @Valid @RequestBody CaseDTO caseDTO) {
        caseDTO.setCaseId(id);
        return updateCaseService.execute(caseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCase(@PathVariable Long id) {
        return deleteCaseService.execute(id);
    }
}