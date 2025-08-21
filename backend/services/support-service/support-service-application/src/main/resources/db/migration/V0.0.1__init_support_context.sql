-- CREATE SCHEMA
CREATE SCHEMA IF NOT EXISTS "support_context";

-- REMOVE TABLES
DROP TABLE IF EXISTS support_context.company_information;
DROP TABLE IF EXISTS support_context.messages;
DROP TABLE IF EXISTS support_context.conversations;
DROP TABLE IF EXISTS support_context.issues;

-- object: support_context.company_information | type: TABLE --
-- DROP TABLE IF EXISTS support_context.company_information CASCADE;
CREATE TABLE support_context.company_information (
	address_line1 varchar(255) NOT NULL,
	address_line2 varchar(255),
	address_line3 varchar(255),
	address_city varchar(255),
	address_postcode varchar(255) NOT NULL,
	address_country smallint NOT NULL,
	support_phone varchar(255) NOT NULL,
	support_phone_business_hours json NOT NULL,
	support_chat_business_hours json NOT NULL,
	support_email varchar(255) NOT NULL,
	website varchar(255) NOT NULL

);
-- ddl-end --
ALTER TABLE support_context.company_information OWNER TO postgres;
-- ddl-end --

-- object: support_context.issues | type: TABLE --
-- DROP TABLE IF EXISTS support_context.issues CASCADE;
CREATE TABLE support_context.issues (
	id uuid NOT NULL,
	subject varchar(255) NOT NULL,
	description text NOT NULL,
	answer text,
	status smallint NOT NULL,
	client uuid NOT NULL,
	reservation uuid,
	updated_at timestamptz NOT NULL,
	CONSTRAINT issue_pk PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON COLUMN support_context.issues.status IS E'Enum';
-- ddl-end --
COMMENT ON COLUMN support_context.issues.client IS E'Client ID (Customer Context)';
-- ddl-end --
COMMENT ON COLUMN support_context.issues.reservation IS E'Reservation ID (Reservation context)';
-- ddl-end --
ALTER TABLE support_context.issues OWNER TO postgres;
-- ddl-end --

-- object: support_context.conversations | type: TABLE --
-- DROP TABLE IF EXISTS support_context.conversations CASCADE;
CREATE TABLE support_context.conversations (
	id uuid NOT NULL,
	subject varchar(255) NOT NULL,
	issue uuid NOT NULL,
	CONSTRAINT conversation_pk PRIMARY KEY (id),
	CONSTRAINT issue_unique UNIQUE (issue)
);
-- ddl-end --
ALTER TABLE support_context.conversations OWNER TO postgres;
-- ddl-end --

-- object: support_context.messages | type: TABLE --
-- DROP TABLE IF EXISTS support_context.messages CASCADE;
CREATE TABLE support_context.messages (
	id uuid NOT NULL,
	message text,
	conversation uuid,
	sender_type smallint NOT NULL,
	sender uuid NOT NULL,
	CONSTRAINT message_pk PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON COLUMN support_context.messages.sender_type IS E'Enum';
-- ddl-end --
COMMENT ON COLUMN support_context.messages.sender IS E'Operator ID or Client ID (User Context)';
-- ddl-end --
ALTER TABLE support_context.messages OWNER TO postgres;
-- ddl-end --

-- object: support_context.faqs | type: TABLE --
-- DROP TABLE IF EXISTS support_context.faqs CASCADE;
CREATE TABLE support_context.faqs (
	id uuid NOT NULL,
	question varchar(255) NOT NULL,
	answer text NOT NULL,
	faq_category varchar(255) NOT NULL,
  updated_at timestamptz NOT NULL,
	CONSTRAINT faq_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE support_context.faqs OWNER TO postgres;
-- ddl-end --
