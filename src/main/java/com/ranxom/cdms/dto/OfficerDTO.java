package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerDTO {
    private Long officersId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Badge number is required")
    private String badgeNumber;

    private String rank;
    private String department;
    private String contact;
    private OffsetDateTime createdAt;
}
