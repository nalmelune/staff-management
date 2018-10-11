CREATE SEQUENCE IF NOT EXISTS DEPARTMENTS_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS WORKERS_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS  DEPARTMENTS(
  ID INT8 default DEPARTMENTS_SEQ.nextval,
  NAME varchar(255) not null,
  primary key(ID)
);

CREATE TABLE IF NOT EXISTS WORKERS
(
   ID INT8 not null,
   FIRST_NAME varchar (255) not null,
   LAST_NAME varchar(255) not null,
   PHONE_NUMBER varchar(255) not null,
   DEPARTMENT_ID INT8,
   primary key(ID),
   foreign key (DEPARTMENT_ID) references DEPARTMENTS(ID)
);

ALTER TABLE WORKERS
  ADD CONSTRAINT uq_names UNIQUE(FIRST_NAME, LAST_NAME);

INSERT INTO DEPARTMENTS (ID,NAME) VALUES (NEXTVAL('DEPARTMENTS_SEQ'),'R&D');
INSERT INTO DEPARTMENTS (ID,NAME) VALUES (NEXTVAL('DEPARTMENTS_SEQ'),'Sales');
INSERT INTO DEPARTMENTS (ID,NAME) VALUES (NEXTVAL('DEPARTMENTS_SEQ'),'HR');
INSERT INTO DEPARTMENTS (ID,NAME) VALUES (NEXTVAL('DEPARTMENTS_SEQ'),'Production');