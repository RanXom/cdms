package com.ranxom.cdms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private Long personsId;

    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate dob;
    private String[] roles;
    private String contact;
    private OffsetDateTime createdAt;
}
