-- liquibase formatted sql

-- changeset yterinc:2

INSERT INTO customers(name) VALUES ('test3');
INSERT INTO customers(name) VALUES ('test5');
INSERT INTO customers(name) VALUES ('test10');

-- changeset yterinc:3
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (1, 'EMAIL', 'test1@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (1, 'PHONE', '8123456789');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (2, 'EMAIL', 'asdfasdf@msd.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (2, 'PHONE', '8912345678');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'EMAIL', 'test3@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'PHONE', '9890123456');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'EMAIL', 'test32@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'PHONE', '9213455678');