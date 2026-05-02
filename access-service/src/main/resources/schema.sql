CREATE TABLE IF NOT EXISTS `access_cards` (
    `access_card_id` int NOT NULL AUTO_INCREMENT,
    `mobile_number` varchar(15) NOT NULL,
    `access_card_number` varchar(100) NOT NULL,
    `access_card_type` varchar(100) NOT NULL, -- π.χ. 'RFID', 'QR_CODE'
    `entry_limit` int NOT NULL,          -- Αντίστοιχο του total_limit
    `entries_used` int NOT NULL,         -- Αντίστοιχο του amount_used
    `remaining_entries` int NOT NULL,    -- Αντίστοιχο του available_amount
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`access_card_id`)
    );