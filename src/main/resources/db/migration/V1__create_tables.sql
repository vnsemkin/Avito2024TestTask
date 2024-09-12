-- Add uuid-ossp extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create employee table
CREATE TABLE IF NOT EXISTS employee
(
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username   VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Create organization_type enum

CREATE TYPE organization_type AS ENUM ('IE', 'LLC', 'JSC');

-- Create organization table
CREATE TABLE IF NOT EXISTS organization
(
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    type        organization_type,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Create organization_responsible table
CREATE TABLE IF NOT EXISTS tenders
(
    id              UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(1000),
    status          VARCHAR(50)  NOT NULL,
    employee_id     UUID         NOT NULL,
    organization_id UUID         NOT NULL,
    service_type    VARCHAR(50)  NOT NULL,
    version         INT          NOT NULL DEFAULT 1,
    created_at      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

-- Create tenders table
CREATE TABLE IF NOT EXISTS organization_responsible
(
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID REFERENCES organization (id) ON DELETE CASCADE,
    user_id         UUID REFERENCES employee (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bids
(
    id          UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status      VARCHAR(50)  NOT NULL CHECK (status IN ('Created', 'Published', 'Canceled')),
    tender_id   UUID         NOT NULL,
    author_type VARCHAR(50)  NOT NULL CHECK (author_type IN ('Organization', 'User')),
    author_id   UUID         NOT NULL,
    version     INT          NOT NULL DEFAULT 1,
    created_at  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);
