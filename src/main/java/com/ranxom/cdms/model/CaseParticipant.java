package com.ranxom.cdms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Table(name = "case_participants")
@IdClass(CaseParticipantId.class)
public class CaseParticipant {

    @Id
    @Column(name = "case_id")
    private Long caseId;

    @Id
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "role_in_case")
    private String roleInCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Cases cases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private Person person;
}
