CREATE TABLE IF NOT EXISTS `member` (
    `member_id` int AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL,
    `mobile_number` varchar(20) NOT NULL,
    -- Εδώ προσθέτουμε τα ιατρικά στοιχεία που συζητήσαμε
    `cardio_cert_expiry` date DEFAULT NULL,
    `derma_cert_expiry` date DEFAULT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS `membership` (
   `member_id` int NOT NULL,
   `membership_number` int AUTO_INCREMENT PRIMARY KEY,
    -- Αντί για account_type, έχουμε membership_type (π.χ. 'GENERAL_ACCESS')
    `membership_type` varchar(100) NOT NULL,
    -- Αντί για branch_address, έχουμε το αθλητικό κέντρο (π.χ. 'EAK_VOLOS')
    `facility_name` varchar(200) NOT NULL,
    `email_sent` BOOLEAN,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );