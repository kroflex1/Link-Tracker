--liquibase formatted sql
--changeset Artyom:2

CREATE TABLE links
(
    link               VARCHAR(300)             NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    last_check_time    TIMESTAMP WITH TIME ZONE NOT NULL,
    last_activity_time TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (link)
)
