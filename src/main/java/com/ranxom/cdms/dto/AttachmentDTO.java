package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Long attachmentId;

    @NotBlank(message = "Filename is required")
    private String filename;

    @NotBlank(message = "URL is required")
    private String url;

    private String checksum;
    private Long uploadedBy;

    @NotNull(message = "Case ID is required")
    private Long caseId;
}
