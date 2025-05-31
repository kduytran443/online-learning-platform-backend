CREATE TABLE price (
    id UUID PRIMARY KEY,
    target_id UUID not null,
    target_type char(2),
    description varchar(512),
    fee_type VARCHAR(1) not null,
    amount DECIMAL(10, 2),
    start_date DATE,
    end_date DATE,
    created_at date NOT NULL,
    created_by varchar(20) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(20) DEFAULT NULL
);
