INSERT INTO company (name,token)
SELECT 'FraGor','qwerty'
WHERE NOT EXISTS (
    SELECT 1 FROM company WHERE name = 'FraGor'
);

INSERT INTO company (name,token)
SELECT 'Random','qwerty2'
WHERE NOT EXISTS (
    SELECT 1 FROM company WHERE name = 'Random'
);