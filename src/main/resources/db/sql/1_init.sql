CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE IF NOT EXISTS user_messages
(
    id bigint not null,
    user_name character varying(50),
    message_id bigint,
    chat_id bigint,
    created_at timestamp with time zone default now() not null,
    CONSTRAINT userMessages_pkey PRIMARY KEY (id)
);