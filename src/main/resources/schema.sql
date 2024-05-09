-- DROP EXTENSION IF EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE if not exists execution_log (
                              id UUID PRIMARY KEY default uuid_generate_v4(),
                              class_name VARCHAR(255) NOT NULL,
                              method_name VARCHAR(255) NOT NULL,
                              execution_time BIGINT NOT NULL,
                              created_at TIMESTAMP NOT NULL,
                              is_async BOOLEAN NOT NULL
);

