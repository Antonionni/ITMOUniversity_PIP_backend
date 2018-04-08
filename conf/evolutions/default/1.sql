# --- 1.sql

# --- !Ups

CREATE TABLE IF NOT EXISTS users (
  id          SERIAL PRIMARY KEY,
  login       VARCHAR(255) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  email       VARCHAR(255) NOT NULL UNIQUE,
  first_name  VARCHAR(255) NOT NULL,
  second_name VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS users;