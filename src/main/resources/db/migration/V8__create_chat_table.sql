create sequence chat_seq start with 1 increment by 50;


CREATE TABLE "chat"
(
    channel_id bigint       NULL,
    created_at timestamp(6) NULL,
    id         bigint       NOT NULL,
    sender_id  bigint       NULL,
    updated_at timestamp(6) NULL,
    image_url  varchar(255) NULL,
    message    varchar(255) NULL,
    CONSTRAINT chat_pkey PRIMARY KEY (id),
    constraint fk_chat_member foreign key (sender_id) references member (id),
    constraint fk_chat_channel foreign key (channel_id) references channel (id)
);