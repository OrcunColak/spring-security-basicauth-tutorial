CREATE TABLE users(
  username varchar_ignorecase(50) NOT NULL PRIMARY KEY,
  password varchar_ignorecase(500) NOT NULL,
  enabled BOOLEAN NOT NULL
  );

CREATE TABLE authorities (
  username VARCHAR_IGNORECASE(50) NOT NULL,
  authority VARCHAR_IGNORECASE(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
  );

CREATE UNIQUE INDEX ix_auth_username ON authorities (username,authority);

--password is 'password' in bcrypt encoding
INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', TRUE);

INSERT INTO users (username, password, enabled) VALUES
    ('user', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', TRUE);

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');