--liquibase formatted sql
--changeset Artyom:3

CREATE TABLE link_and_chat
(
    id      SERIAL       NOT NULL,
    link    VARCHAR(300) NOT NULL,
    chat_id BIGINT       NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (link) REFERENCES links (link),
    FOREIGN KEY (chat_id) REFERENCES chats (chat_id)
)
