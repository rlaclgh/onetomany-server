create sequence chat_room_tag_seq start with 1 increment by 50;

CREATE TABLE "chat_room_tag"
(
    id           bigint NOT NULL,
    chat_room_id bigint NULL,
    tag_id       bigint NULL,
    CONSTRAINT chat_room_tag_pkey PRIMARY KEY (id),
    constraint fk_chat_room_tag_chat_room foreign key (chat_room_id) references chat_room (id),
    constraint fk_chat_room_tag_tag foreign key (tag_id) references tag (id)

);