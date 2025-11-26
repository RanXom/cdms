package com.ranxom.cdms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Table(name = "officers")
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "officers_id")
    private Long officersId;

    @Column(name = "name")
    private String name;

    @Column(name = "badge_number")
    private String badgeNumber;

    @Column(name = "rank")
    private String rank;

    @Column(name = "department")
    private String department;

    @Column(name = "contact", columnDefinition="jsonb")
    private String contact;

    @Column(name = "created_at", columnDefinition="TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }

}