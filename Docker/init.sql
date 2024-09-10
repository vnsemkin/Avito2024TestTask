CREATE USER avito WITH PASSWORD 'avito';
CREATE DATABASE avito;
GRANT ALL PRIVILEGES ON DATABASE avito TO avito;

\c avito

-- Ensure the user has the necessary schema-level permissions
GRANT USAGE ON SCHEMA public TO avito;
GRANT CREATE ON SCHEMA public TO avito;