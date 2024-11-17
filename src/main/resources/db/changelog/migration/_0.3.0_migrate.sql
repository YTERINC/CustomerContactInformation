-- liquibase formatted sql

-- changeset yterinc:4

INSERT INTO customers(name) VALUES ('test555');
INSERT INTO customers(name) VALUES ('test777');
INSERT INTO customers(name) VALUES ('test888');

-- changeset yterinc:5
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (1, 'EMAIL', 'test777@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (1, 'PHONE', '8123456789');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (2, 'EMAIL', 'asdfasdf77@msd.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (2, 'PHONE', '8912345678');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'EMAIL', 'test377@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'PHONE', '9890123456');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'EMAIL', 'test32777@mail.com');
INSERT INTO contacts(customer_id, contact_type, contact_value) VALUES (3, 'PHONE', '9213455678');