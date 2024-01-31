create sequence tag_seq start with 1 increment by 50;


CREATE TABLE "tag"
(
    created_at timestamp(6) NULL,
    id         bigint       NOT NULL,
    updated_at timestamp(6) NULL,
    name       varchar(255) NULL,
    CONSTRAINT tag_pkey PRIMARY KEY (id)
);