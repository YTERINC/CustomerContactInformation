-- liquibase formatted sql

-- changeset yterinc:1

CREATE TABLE IF NOT EXISTS Customers
(
    id   INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(70) NOT NULL
);

CREATE TABLE IF NOT EXISTS Contacts
(
    id            INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    customer_id   INT REFERENCES Customers (id) ON DELETE CASCADE,
    contact_type  VARCHAR(50) NOT NULL,
    contact_value VARCHAR(50) NOT NULL
);