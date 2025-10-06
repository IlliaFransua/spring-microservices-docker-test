DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mini_test_db') THEN
      PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE mini_test_db');
   END IF;
EXCEPTION WHEN undefined_function THEN
   BEGIN
      CREATE DATABASE mini_test_db;
   EXCEPTION WHEN duplicate_database THEN
      NULL;
   END;
END
$$ LANGUAGE plpgsql;

DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'auth_api_rw') THEN
      CREATE ROLE auth_api_rw WITH LOGIN PASSWORD 'Yn1Ef7Cqhz3e';
   ELSE
      ALTER ROLE auth_api_rw WITH PASSWORD 'Yn1Ef7Cqhz3e';
   END IF;
END
$$ LANGUAGE plpgsql;

\connect mini_test_db
GRANT CONNECT ON DATABASE mini_test_db TO auth_api_rw;
GRANT USAGE ON SCHEMA public TO auth_api_rw;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO auth_api_rw;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO auth_api_rw;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO auth_api_rw;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO auth_api_rw;
GRANT CREATE ON SCHEMA public TO auth_api_rw;

