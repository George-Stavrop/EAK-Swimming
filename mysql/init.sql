-- Δημιουργία Βάσεων
CREATE DATABASE IF NOT EXISTS membership_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS subscription_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS access_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Δημιουργία Χρήστη για το Membership Service
CREATE USER IF NOT EXISTS 'membership_user'@'%' IDENTIFIED BY 'membership';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON membership_db.* TO 'membership_user'@'%';

-- Δημιουργία Χρήστη για το Subscription Service
CREATE USER IF NOT EXISTS 'subscription_user'@'%' IDENTIFIED BY 'subscription';GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON subscription_db.* TO 'subscription_user'@'%';

-- Δημιουργία Χρήστη για το Access Service
CREATE USER IF NOT EXISTS 'access_user'@'%' IDENTIFIED BY 'access';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON access_db.* TO 'access_user'@'%';

FLUSH PRIVILEGES;