--liquibase formatted sql
--changeset Artyom:1

CREATE TABLE chats
(
    chat_id    BIGINT                   NOT NULL,
    created_at timestamp with time zone not null NOT NULL,

    PRIMARY KEY (chat_id)
)
