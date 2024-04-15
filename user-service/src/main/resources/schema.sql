CREATE TABLE IF NOT EXISTS `userdata` (
    `seq` int AUTO_INCREMENT,
    `id` UUID PRIMARY KEY,
    `username` varchar(200) NOT NULL,
    `name` varchar(200) NOT NULL,
    `email` varchar(200) NOT NULL,
    `user_type` varchar(100) NOT NULL,
    `mobile_phone` varchar(13) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );
