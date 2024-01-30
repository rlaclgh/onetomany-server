create sequence feedback_seq start with 1 increment by 50;

CREATE TABLE "feedback"
(
    created_at  timestamp(6) NULL,
    id          bigint       NOT NULL,
    updated_at  timestamp(6) NULL,
    description varchar(255) NULL,
    CONSTRAINT feedback_pkey PRIMARY KEY (id)
);