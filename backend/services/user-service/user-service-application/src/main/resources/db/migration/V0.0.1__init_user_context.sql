-- CREATE SCHEMA
CREATE SCHEMA IF NOT EXISTS "user_context";

-- REMOVE TABLES
DROP TABLE IF EXISTS user_context.clients;
DROP TABLE IF EXISTS user_context.credentials;
DROP TABLE IF EXISTS user_context.operators;


-- object: user_context.clients | type: TABLE --
-- DROP TABLE IF EXISTS user_context.clients CASCADE;
CREATE TABLE user_context.clients (
	id uuid NOT NULL,
	last_name varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	phone varchar(255) NOT NULL,
	birthdate varchar(255) NOT NULL,
	address_line1 varchar(255) NOT NULL,
	address_line2 varchar(255),
	address_line3 varchar(255),
	address_city varchar(255) NOT NULL,
	address_postcode varchar(255) NOT NULL,
	address_country smallint NOT NULL,
	credential uuid,
	updated_at timestamptz NOT NULL,
	deleted_at timestamptz,
	CONSTRAINT clients_pk PRIMARY KEY (id),
	CONSTRAINT credential_client_unique UNIQUE (credential)
);
-- ddl-end --
ALTER TABLE user_context.clients OWNER TO postgres;
-- ddl-end --

-- object: user_context.credentials | type: TABLE --
-- DROP TABLE IF EXISTS user_context.credentials CASCADE;
CREATE TABLE user_context.credentials (
	id uuid NOT NULL,
	last_connection timestamptz NOT NULL,
	hashed_identifier varchar(250),
	hashed_password varchar(350) NOT NULL,
	sso_id varchar(450),
	sso_provider smallint,
	topt_code_value varchar(350),
	passkey_alg varchar(255),
	passkey_publickey text,
	CONSTRAINT credentials_pk PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON COLUMN user_context.credentials.hashed_identifier IS E'With no Salt or Pepper';
-- ddl-end --
COMMENT ON COLUMN user_context.credentials.hashed_password IS E'With Salt';
-- ddl-end --
COMMENT ON COLUMN user_context.credentials.sso_id IS E'Encrypted';
-- ddl-end --
COMMENT ON COLUMN user_context.credentials.sso_provider IS E'Enum SsoProvider';
-- ddl-end --
COMMENT ON COLUMN user_context.credentials.topt_code_value IS E'Encrypted';
-- ddl-end --
ALTER TABLE user_context.credentials OWNER TO postgres;
-- ddl-end --

-- object: user_context.operators | type: TABLE --
-- DROP TABLE IF EXISTS user_context.operators CASCADE;
CREATE TABLE user_context.operators (
	id uuid NOT NULL,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	roles jsonb NOT NULL,
	credential uuid NOT NULL,
	updated_at timestamptz NOT NULL,
	deleted_at timestamptz,
	CONSTRAINT operators_pk PRIMARY KEY (id),
	CONSTRAINT credential_operator_unique UNIQUE (credential)
);
-- ddl-end --
COMMENT ON COLUMN user_context.operators.roles IS E'Set<Role>';
-- ddl-end --
ALTER TABLE user_context.operators OWNER TO postgres;
-- ddl-end --
