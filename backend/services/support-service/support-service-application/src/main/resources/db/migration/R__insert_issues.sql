INSERT INTO
  support_context.issues (
    id,
    subject,
    description,
    answer,
    status,
    client,
    reservation,
    updated_at
  )
VALUES
  (
    '0198f290-8e6d-7cdc-b3a2-feffbd0d21f7',
    'Support Request 1',
    'I need help with my account.',
    NULL,
    1,
    '019159f8-d42f-7000-8000-000000000002',
    '0198f282-e472-7985-aec0-72d8408e3f8c',
    '2025-08-28T21:22:08.406Z'
  ),
  (
    '0198f290-c420-75ad-b251-2a15435391c3',
    'Support Request 2',
    'I have an issue with my booking.',
    'The issue has been resolved in shop by the customer.',
    2,
    '019159f8-d42f-7000-8000-000000000002',
    '0198f282-e472-72da-a876-36d72056f849',
    '2025-08-28T21:22:08.406Z'
  );
