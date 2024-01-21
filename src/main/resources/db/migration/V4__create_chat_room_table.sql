CREATE TABLE "chat_room"
(
    created_at  timestamp(6) NULL,
    id          int8         NOT NULL,
    owner_id    int8         NULL,
    updated_at  timestamp(6) NULL,
    description varchar(255) NULL,
    image_url   varchar(255) NULL,
    "name"      varchar(255) NULL,
    CONSTRAINT chat_room_pkey PRIMARY KEY (id)
);