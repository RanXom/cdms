package com.ranxom.cdms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Table(name = "investigation_instances")
@IdClass(InvestigationInstanceId.class)
public class InvestigationInstance {

    @Id
    @Column(name = "year", nullable = false)
    private Integer year;

    @Id
    @Column(name = "quarter", nullable = false)
    private Integer quarter;

    @Id
    @Column(name = "case_id", nullable = false)
    private Long caseId;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", insertable = false, updatable = false)
    private Cases cases;
}
