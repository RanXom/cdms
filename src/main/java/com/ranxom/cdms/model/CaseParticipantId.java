package com.ranxom.cdms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseParticipantId implements Serializable {
    private Long caseId;
    private Long personId;
}
