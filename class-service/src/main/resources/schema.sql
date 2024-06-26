CREATE TABLE IF NOT EXISTS class (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    start_at date NOT NULL,
    end_at date,
    password varchar(512),
    status char(1),
    category_id UUID NOT NULL,
    owner_type char(1),
    image varchar(200) NOT NULL,
    owner_id UUID,
    owner_name varchar(100) NOT NULL,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS class_description (
     id UUID PRIMARY KEY,
     content text NOT NULL,
     class_id UUID,
     foreign key (class_id) references class(id)
);