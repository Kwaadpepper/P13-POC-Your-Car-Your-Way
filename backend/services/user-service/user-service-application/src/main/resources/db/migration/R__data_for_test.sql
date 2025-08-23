INSERT INTO user_context.operators (id, name, email, roles, credential, updated_at, deleted_at)
VALUES (
  '019159f8-d42f-7000-8000-000000000001',
  'Jane Doe Operator',
  'user@example.net',
  '["SUPPORT"]'::jsonb,
  '019159f8-d42f-7000-8000-000000000010',
  '2025-08-22 23:05:18.808000',
  NULL
);

INSERT INTO user_context.credentials (id, last_connection, hashed_identifier, hashed_password, sso_id, sso_provider, topt_code_value, passkey_id, passkey_publickey, passkey_sign_count, passkey_type)
VALUES (
  '019159f8-d42f-7000-8000-000000000010',
  NULL,
  '01332c876518a793b7c1b8dfaf6d4b404ff5db09b21c6627ca59710cc24f696a',
  '$argon2id$v=19$m=16384,t=2,p=1$W8AuZ3AvUE35NMcgKjjlbw$8dZCMU1eMt+2/N2fBnLXBlIiAP+JqAbfMw4sVxmImiY',
  NULL, NULL, NULL, NULL,
  NULL,
  NULL, NULL
);

INSERT INTO user_context.clients (id, last_name, first_name, email, phone, birthdate, address_line1, address_line2, address_line3, address_city, address_postcode, address_country, credential, updated_at, deleted_at)
VALUES (
  '019159f8-d42f-7000-8000-000000000002',
  'Doe',
  'John',
  'user@example.net',
  '+1 305-977-7696',
  '1993-04-19',
  '27477 Goodwin Creek',
  NULL, NULL,
  'Dorseystad',
  '57850',
  'FR',
  '019159f8-d42f-7000-8000-000000000020',
  '2025-08-22 23:05:18.238000',
  NULL
);

INSERT INTO user_context.credentials (id, last_connection, hashed_identifier, hashed_password, sso_id, sso_provider, topt_code_value, passkey_id, passkey_publickey, passkey_sign_count, passkey_type)
VALUES (
  '019159f8-d42f-7000-8000-000000000020',
  NULL,
  '6cea57c2fb6cbc2a40411135005760f241fffc3e5e67ab99882726431037f908',
  '$argon2id$v=19$m=16384,t=2,p=1$qryx/ImtQxg5Zce6qIMJCQ$T4sJ3jS0J7vhuoeBfBwi0VtxOVb1Up3ZbitHwTcbGHQ',
  NULL, NULL, NULL, NULL,
  NULL,
  NULL, NULL
);
