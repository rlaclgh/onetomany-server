create sequence chat_room_seq start with 1 increment by 50;


CREATE TABLE "chat_room"
(
    created_at  timestamp(6) NULL,
    id          bigint       NOT NULL,
    owner_id    bigint       NULL,
    updated_at  timestamp(6) NULL,
    description varchar(255) NULL,
    image_url   varchar(255) NULL,
    "name"      varchar(255) NULL,
    CONSTRAINT chat_room_pkey PRIMARY KEY (id),

    constraint fk_chat_room_member foreign key (owner_id) references member (id)
);