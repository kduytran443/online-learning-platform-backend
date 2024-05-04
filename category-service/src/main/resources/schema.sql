CREATE TABLE IF NOT EXISTS category (
    id UUID PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(500),
    category_id UUID,
    category_status varchar(1),
    foreign key (category_id) references category(id)
);
