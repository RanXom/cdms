-- V1__Init_Schema.sql
-- This script creates the initial database schema based on the project ER diagram.

-- Roles table for Role-Based Access Control (RBAC)
CREATE TABLE roles (
                       role_name TEXT PRIMARY KEY,
                       permissions JSONB
);

-- Users table for authentication and linking activity
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
                       salt TEXT NOT NULL,
                       email TEXT NOT NULL UNIQUE,
                       role TEXT REFERENCES roles(role_name),
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       last_login TIMESTAMP WITH TIME ZONE
);

-- Officers table
CREATE TABLE officers (
                          officers_id BIGSERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          badge_number TEXT NOT NULL UNIQUE,
                          rank TEXT,
                          department TEXT,
                          contact JSONB,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Cases table, the central entity
CREATE TABLE cases (
                       case_id BIGSERIAL PRIMARY KEY,
                       title TEXT NOT NULL,
                       description TEXT,
                       case_type TEXT,
                       status TEXT NOT NULL DEFAULT 'open',
                       reported_at TIMESTAMP WITH TIME ZONE,
                       lead_officer_id BIGINT REFERENCES officers(officers_id),
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       location TEXT,
                       severity TEXT
);

-- Persons table (suspects, victims, witnesses)
CREATE TABLE persons (
                         persons_id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         dob DATE,
                         roles TEXT[],
                         contact JSONB,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- case_participants (Join table for cases and persons)
CREATE TABLE case_participants (
                                   case_id BIGINT NOT NULL REFERENCES cases(case_id),
                                   person_id BIGINT NOT NULL REFERENCES persons(persons_id),
                                   role_in_case TEXT,
                                   PRIMARY KEY (case_id, person_id)
);

-- Attachments (reports, documents)
CREATE TABLE attachments (
                             attachment_id BIGSERIAL PRIMARY KEY,
                             filename TEXT NOT NULL,
                             url TEXT NOT NULL,
                             checksum TEXT,
                             uploaded_by BIGINT REFERENCES users(id),
                             case_id BIGINT NOT NULL REFERENCES cases(case_id)
);

-- case_notes (running log for a case)
CREATE TABLE case_notes (
                            note_id BIGSERIAL PRIMARY KEY,
                            author_id BIGINT REFERENCES users(id),
                            content TEXT,
                            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            case_id BIGINT NOT NULL REFERENCES cases(case_id)
);

-- investigation_instances
CREATE TABLE investigation_instances (
                                         year INT NOT NULL,
                                         quarter INT NOT NULL,
                                         case_id BIGINT NOT NULL REFERENCES cases(case_id),
                                         started_at TIMESTAMP WITH TIME ZONE,
                                         closed_at TIMESTAMP WITH TIME ZONE,
                                         PRIMARY KEY (year, quarter, case_id)
);

-- evidence (physical, digital)
CREATE TABLE evidence (
                          evidence_id BIGSERIAL PRIMARY KEY,
                          case_id BIGINT NOT NULL REFERENCES cases(case_id),
                          type TEXT,
                          metadata JSONB,
                          stored_at TEXT,
                          chain_of_custody INT,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- audit_logs for immutable tracking
CREATE TABLE audit_logs (
                            audit_id BIGSERIAL PRIMARY KEY,
                            entity_type TEXT,
                            entity_id TEXT,
                            action TEXT,
                            changed_by TEXT,
                            change_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            diff JSONB
);

-- ### INDEXES ###
-- Add indexes for common query fields and foreign keys for performance

-- Cases
CREATE INDEX idx_cases_status ON cases(status);
CREATE INDEX idx_cases_lead_officer_id ON cases(lead_officer_id);

-- Persons
CREATE INDEX idx_persons_name ON persons(name);
CREATE INDEX idx_persons_roles ON persons USING GIN(roles);

-- Evidence
CREATE INDEX idx_evidence_case_id ON evidence(case_id);
CREATE INDEX idx_evidence_metadata ON evidence USING GIN(metadata);

-- Attachments
CREATE INDEX idx_attachments_case_id ON attachments(case_id);

-- Case Notes
CREATE INDEX idx_case_notes_case_id ON case_notes(case_id);

-- Audit Logs
CREATE INDEX idx_audit_logs_entity_type_id ON audit_logs(entity_type, entity_id);