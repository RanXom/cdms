package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDTO {
    private Long caseId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String caseType;
    private String status;
    private OffsetDateTime reportedAt;
    private Long leadOfficerId;
    private OffsetDateTime createdAt;
    private String location;
    private String severity;
}
