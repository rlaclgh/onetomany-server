CREATE TABLE "channel"
(
    is_host      bool         NULL,
    chat_room_id int8         NULL,
    created_at   timestamp(6) NULL,
    id           int8         NOT NULL,
    owner_id     int8         NULL,
    updated_at   timestamp(6) NULL,
    CONSTRAINT channel_pkey PRIMARY KEY (id)
);