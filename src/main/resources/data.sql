create table users(
  username varchar_ignorecase(50) not null primary key,
  password varchar_ignorecase(500) not null,
  enabled boolean not null
  );

create table authorities (
  username varchar_ignorecase(50) not null,
  authority varchar_ignorecase(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username)
  );

create unique index ix_auth_username on authorities (username,authority);

--password is 'password' in bcrypt encoding
INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', TRUE);

INSERT INTO users (username, password, enabled) VALUES
    ('user', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', TRUE);

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER');