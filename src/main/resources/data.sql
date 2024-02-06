--password is 'password' in bcrypt encoding
insert into users (username, password, enabled) values
    ('admin', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', TRUE);

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');

-- JPA
insert into customers (email, password, firstname, surname, phone)
values ('orcun@example.com', '$2a$10$8fwn0LUKql6wTzJHO2QoQ.Nd.59eIyFwaucgBJoiZ/T5SqrqNmyBm', 'Orçun', 'Çolak', '12345');

insert into roles (id, authority) values (1, 'ROLE_ADMIN');
insert into roles (id, authority) values (2, 'ROLE_USER');

insert into customer_role (customer_id, role_id) values (1, 1);
insert into customer_role (customer_id, role_id) values (1, 2);
