CREATE TABLE "chat"
(
    channel_id int8         NULL,
    created_at timestamp(6) NULL,
    id         int8         NOT NULL,
    sender_id  int8         NULL,
    updated_at timestamp(6) NULL,
    image_url  varchar(255) NULL,
    message    varchar(255) NULL,
    CONSTRAINT chat_pkey PRIMARY KEY (id)
);