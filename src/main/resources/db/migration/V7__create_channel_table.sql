create sequence channel_seq start with 1 increment by 50;

CREATE TABLE "channel"
(
    is_host      bool         NULL,
    chat_room_id bigint       NULL,
    created_at   timestamp(6) NULL,
    id           bigint       NOT NULL,
    owner_id     bigint       NULL,
    updated_at   timestamp(6) NULL,
    CONSTRAINT channel_pkey PRIMARY KEY (id),
    constraint fk_channel_member foreign key (owner_id) references member (id),
    constraint fk_channel_chat_room foreign key (chat_room_id) references chat_room (id)

);