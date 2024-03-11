--liquibase formatted sql
--changeset Artyom:2

CREATE TABLE links
(
    link             VARCHAR(300) NOT NULL,
    created_time     TIMESTAMP    NOT NULL,
    last_time_update TIMESTAMP    NOT NULL,
    PRIMARY KEY (link)
)
