package com.ranxom.cdms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name="officers")
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="officers_id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="badge_number")
    private String badgeNumber;

    @Column(name="rank")
    private String rank;

    @Column(name="department")
    private String department;

    @Column(name="contact", columnDefinition="jsonb")
    private String contact;

    @Column(name="created_at", columnDefinition="TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }

}