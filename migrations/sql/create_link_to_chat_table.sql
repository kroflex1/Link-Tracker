--liquibase formatted sql
--changeset Artyom:3

CREATE TABLE link_and_chat
(
    link    VARCHAR(300) NOT NULL,
    chat_id BIGINT       NOT NULL,

    PRIMARY KEY (link, chat_id),
    FOREIGN KEY (link) REFERENCES links (link) ON DELETE CASCADE,
    FOREIGN KEY (chat_id) REFERENCES chats (chat_id) ON DELETE CASCADE
)
