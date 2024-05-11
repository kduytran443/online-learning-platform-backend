CREATE TABLE IF NOT EXISTS class (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    start_at date NOT NULL,
    end_at date,
    status char(1),
    category_ids UUID[] NOT NULL,
    group_id UUID,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS class_category (
    id UUID PRIMARY KEY,
    content text NOT NULL,
    class_id UUID NOT NULL,
    foreign key (class_id) references class(id)
);
