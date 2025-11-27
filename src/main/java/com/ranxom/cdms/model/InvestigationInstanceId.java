package com.ranxom.cdms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationInstanceId implements Serializable {
    private Integer year;
    private Integer quarter;
    private Long caseId;
}
