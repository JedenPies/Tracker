-- Database: tracker

-- DROP DATABASE tracker;

CREATE DATABASE tracker
  WITH OWNER = tracker_admin
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Polish_Poland.1250'
       LC_CTYPE = 'Polish_Poland.1250'
       CONNECTION LIMIT = -1;
       
       
-- Sequence: coordinates_seq

-- DROP SEQUENCE coordinates_seq;

CREATE SEQUENCE coordinates_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE coordinates_seq
  OWNER TO postgres;
GRANT ALL ON TABLE coordinates_seq TO postgres;
GRANT USAGE ON TABLE coordinates_seq TO public;



-- Table: coordinates

-- DROP TABLE coordinates;

CREATE TABLE coordinates
(
  id integer NOT NULL,
  longitude double precision NOT NULL,
  latitude double precision NOT NULL,
  "timestamp" timestamp with time zone NOT NULL,
  CONSTRAINT coordinates_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE coordinates
  OWNER TO tracker_admin;
GRANT ALL ON TABLE coordinates TO tracker_admin;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE coordinates TO public;



-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
  id integer NOT NULL,
  username character varying(50) NOT NULL,
  password character varying(100) NOT NULL,
  email character varying(200),
  CONSTRAINT users_pk PRIMARY KEY (id),
  CONSTRAINT users_username_uq UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO tracker_admin;
GRANT ALL ON TABLE users TO tracker_admin;
GRANT SELECT, UPDATE, INSERT ON TABLE users TO public;

  
  
  
-- Sequence: users_seq

-- DROP SEQUENCE users_seq;

CREATE SEQUENCE users_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE users_seq
  OWNER TO tracker_admin;
GRANT ALL ON TABLE users_seq TO tracker_admin;
GRANT USAGE ON TABLE users_seq TO public;
  