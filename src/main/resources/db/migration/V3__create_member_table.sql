CREATE TABLE "member"
(
    role_id    int4         NULL,
    created_at timestamp(6) NULL,
    id         int8         NOT NULL,
    updated_at timestamp(6) NULL,
    email      varchar(255) NULL,
    "password" varchar(255) NULL,
    CONSTRAINT member_email_key UNIQUE (email),
    CONSTRAINT member_pkey PRIMARY KEY (id)
);