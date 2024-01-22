CREATE TABLE "authority" (
      id int4 NOT NULL,
      role_id int4 NULL,
      "name" varchar(255) NULL,

      CONSTRAINT authority_pkey PRIMARY KEY (id),
    CONSTRAINT fk_authority_role foreign key (role_id) references role(id)
);


-- CONSTRAINT fk_address_person FOREIGN KEY (person_id) REFERENCES person(id)



