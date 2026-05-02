CREATE TABLE IF NOT EXISTS `subscriptions` (
    `subscription_id` int NOT NULL AUTO_INCREMENT,
    `mobile_number` varchar(15) NOT NULL,
    `subscription_number` varchar(100) NOT NULL,
    `subscription_type` varchar(100) NOT NULL,
    `amount_paid` decimal(10,2) NOT NULL,
    `start_date` date NOT NULL,
    `end_date` date NOT NULL,
    `created_at` datetime NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`subscription_id`)
    );