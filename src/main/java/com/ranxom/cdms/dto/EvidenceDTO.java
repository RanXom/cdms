package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvidenceDTO {
    private Long evidenceId;

    @NotNull(message = "Case ID is required")
    private Long caseId;

    private String type;
    private String metadata;
    private String storedAt;
    private Integer chainOfCustody;
    private OffsetDateTime createdAt;
}
