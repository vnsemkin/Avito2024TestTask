-- Create employee table
CREATE TABLE IF NOT EXISTS employee (
                                        id SERIAL PRIMARY KEY,
                                        username VARCHAR(50) UNIQUE NOT NULL,
                                        first_name VARCHAR(50),
                                        last_name VARCHAR(50),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create organization_type enum
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'organization_type') THEN
        CREATE TYPE organization_type AS ENUM ('IE', 'LLC', 'JSC');
    END IF;
END $$;

-- Create organization table
CREATE TABLE IF NOT EXISTS organization (
                                            id SERIAL PRIMARY KEY,
                                            name VARCHAR(100) NOT NULL,
                                            description TEXT,
                                            type organization_type,
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create organization_responsible table
CREATE TABLE IF NOT EXISTS organization_responsible (
                                                        id SERIAL PRIMARY KEY,
                                                        organization_id INT REFERENCES organization(id) ON DELETE CASCADE,
                                                        user_id INT REFERENCES employee(id) ON DELETE CASCADE
);

-- Create tenders table
CREATE TABLE IF NOT EXISTS tenders (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       description VARCHAR(1000),
                                       status VARCHAR(50) NOT NULL,
                                       service_type VARCHAR(50) NOT NULL,
                                       version INT NOT NULL,
                                       created_at TIMESTAMP NOT NULL,
                                       updated_at TIMESTAMP NOT NULL
);
