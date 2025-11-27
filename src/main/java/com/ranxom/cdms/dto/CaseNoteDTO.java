package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseNoteDTO {
    private Long noteId;
    private Long authorId;

    @NotBlank(message = "Content is required")
    private String content;

    private OffsetDateTime createdAt;

    @NotNull(message = "Case ID is required")
    private Long caseId;
}
