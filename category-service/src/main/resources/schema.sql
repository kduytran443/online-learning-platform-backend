
CREATE TABLE IF NOT EXISTS `category` (
    `id` UUID PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `description` varchar(500),
    `category_id` UUID,
    `status` varchar(1),

    FOREIGN KEY (`category_id`) REFERENCES `category`(id)
);
