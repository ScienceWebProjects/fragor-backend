--/////////////////////////////////////////COMPANY///////////////////////////////////////////////////
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
--/////////////////////////////////////////ADDED DEVICE//////////////////////////////////////////////
INSERT INTO configuration_entity (key,value)
SELECT 'addedDeviceAddedCommand','dnhevwauior78q49nvfpoidbnklgfdoY%^73ciovmlfds'
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'addedDeviceCommand'
);
INSERT INTO configuration_entity (key,value)
SELECT 'addedDevicePort',6842
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'addedDevicePort'
);
INSERT INTO configuration_entity (key,value)
SELECT 'addedDeviceAddedPassword','avnrhewirb78943qnvpofdpgfv'
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'addedDevicePassword'
);
--/////////////////////////////////////////MEASUREMENT DEVICE//////////////////////////////////////////////

INSERT INTO configuration_entity (key,value)
SELECT 'measurementDeviceAddedCommand','436bq57896bn47v80cgmbuiwshmxipojUVY4W785263Q9B'
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'measurementDevicePassword'
);

INSERT INTO configuration_entity (key,value)
SELECT 'measurementDevicePort',3984
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'measurementDevicePort'
);

INSERT INTO configuration_entity (key,value)
SELECT 'measurementDeviceAddedPassword','NBFDSHFV9W8Q657893Vfhve78w9aq349bdsfvsd94'
WHERE NOT EXISTS (
    SELECT 1 FROM configuration_entity WHERE key = 'measurementDeviceAddedPassword'
);