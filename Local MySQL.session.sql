SELECT DATABASE();
SHOW TABlES;

use church_management;



SELECT
    payment_id,
    contribution_id,
    amount,
    status,
    provider,
    transaction_reference,
    provider_reference,
    updated_at
FROM payments
WHERE payment_id = 4;


SELECT
    contribution_id,
    contribution_type,
    amount,
    contribution_date,
    member_id
FROM contributions
ORDER BY contribution_id DESC
LIMIT 5;


SELECT
    payment_id,
    contribution_id,
    amount,
    method,
    status,
    provider,
    transaction_reference,
    created_at
FROM payments
WHERE contribution_id = 17;


SELECT
    payment_id,
    contribution_id,
    amount,
    method,
    status,
    provider,
    transaction_reference,
    updated_at
FROM payments
WHERE payment_id = 7;
