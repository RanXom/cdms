-- V1__Init_Schema.sql
-- Initial schema setup + base seed data

-- ===============================
-- 📌 TABLES
-- ===============================

-- Roles table for RBAC
CREATE TABLE roles (
                       role_name TEXT PRIMARY KEY,
                       permissions JSONB
);

-- Users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
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

-- Cases table
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

-- Persons table
CREATE TABLE persons (
                         persons_id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         dob DATE,
                         roles TEXT[],
                         contact JSONB,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Case participants
CREATE TABLE case_participants (
                                   case_id BIGINT NOT NULL REFERENCES cases(case_id),
                                   person_id BIGINT NOT NULL REFERENCES persons(persons_id),
                                   role_in_case TEXT,
                                   PRIMARY KEY (case_id, person_id)
);

-- Attachments
CREATE TABLE attachments (
                             attachment_id BIGSERIAL PRIMARY KEY,
                             filename TEXT NOT NULL,
                             url TEXT NOT NULL,
                             checksum TEXT,
                             uploaded_by BIGINT REFERENCES users(id),
                             case_id BIGINT NOT NULL REFERENCES cases(case_id)
);

-- Case notes
CREATE TABLE case_notes (
                            note_id BIGSERIAL PRIMARY KEY,
                            author_id BIGINT REFERENCES users(id),
                            content TEXT,
                            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            case_id BIGINT NOT NULL REFERENCES cases(case_id)
);

-- Investigation instances
CREATE TABLE investigation_instances (
                                         year INT NOT NULL,
                                         quarter INT NOT NULL,
                                         case_id BIGINT NOT NULL REFERENCES cases(case_id),
                                         started_at TIMESTAMP WITH TIME ZONE,
                                         closed_at TIMESTAMP WITH TIME ZONE,
                                         PRIMARY KEY (year, quarter, case_id)
);

-- Evidence
CREATE TABLE evidence (
                          evidence_id BIGSERIAL PRIMARY KEY,
                          case_id BIGINT NOT NULL REFERENCES cases(case_id),
                          type TEXT,
                          metadata JSONB,
                          stored_at TEXT,
                          chain_of_custody INT,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Audit logs
CREATE TABLE audit_logs (
                            audit_id BIGSERIAL PRIMARY KEY,
                            entity_type TEXT,
                            entity_id TEXT,
                            action TEXT,
                            changed_by TEXT,
                            change_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            diff JSONB
);

-- ===============================
-- 📌 INDEXES
-- ===============================

CREATE INDEX idx_cases_status ON cases(status);
CREATE INDEX idx_cases_lead_officer_id ON cases(lead_officer_id);

CREATE INDEX idx_persons_name ON persons(name);
CREATE INDEX idx_persons_roles ON persons USING GIN(roles);

CREATE INDEX idx_evidence_case_id ON evidence(case_id);
CREATE INDEX idx_evidence_metadata ON evidence USING GIN(metadata);

CREATE INDEX idx_attachments_case_id ON attachments(case_id);

CREATE INDEX idx_case_notes_case_id ON case_notes(case_id);

CREATE INDEX idx_audit_logs_entity_type_id ON audit_logs(entity_type, entity_id);

-- ===============================
-- 🚀 SEED DATA INSERTION
-- ===============================

-- Roles
INSERT INTO roles (role_name, permissions) VALUES
   ('ADMIN', '{"read": true, "write": true, "delete": true}'::jsonb),
   ('USER', '{"read": true, "write": false, "delete": false}'::jsonb),
   ('OFFICER', '{"read": true, "write": true, "delete": false}'::jsonb);

-- Officers
INSERT INTO officers (name, badge_number, rank, department, contact)
VALUES
    ('Akhil Singh', 'B001', 'Inspector', 'Cyber Crime', '{"phone": "111-111"}'),
    ('Nina Kapoor', 'B002', 'Sub-Inspector', 'Robbery', '{"phone": "222-222"}');

-- Cases
INSERT INTO cases (title, description, case_type, status, reported_at, lead_officer_id, location, severity)
VALUES
    ('ATM Skimming Operation', 'Multiple ATM machines compromised using skimming devices.', 'HOMICIDE', 'under_investigation', NOW() - INTERVAL '3 days', 1, 'Sector 18, Noida', 'HIGH'),
    ('Illegal Arms Shipment', 'Suspicious cargo container found with illegal firearms.', 'OTHER', 'under_investigation', NOW() - INTERVAL '7 days', 2, 'Nhava Sheva Port, Mumbai', 'CRITICAL'),
    ('Kidnapping Case', 'Young boy reported missing from neighborhood.', 'HOMICIDE', 'open', NOW() - INTERVAL '2 days', 1, 'Dwarka, Delhi', 'HIGH'),
    ('Diamond Heist', 'High-profile robbery at luxury jewellery showroom.', 'FRAUD', 'closed', NOW() - INTERVAL '14 days', 2, 'Bandra, Mumbai', 'CRITICAL'),
    ('Stolen Bike', 'Motorbike reported stolen outside a metro station.', 'THEFT', 'closed', NOW() - INTERVAL '5 days', 1, 'Rajouri Garden, Delhi', 'MEDIUM'),
    ('Credit Card Fraud', 'Multiple unauthorized transactions reported.', 'OTHER', 'closed', NOW() - INTERVAL '1 day', 2, 'Online', 'LOW');