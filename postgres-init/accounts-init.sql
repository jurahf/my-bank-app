CREATE SCHEMA IF NOT EXISTS accounts;

CREATE TABLE IF NOT EXISTS accounts.Users
(
    Id varchar(100) not null primary key, -- он же login
    Name varchar(255) not null,
    BirthDate date not null,
    Money decimal (9, 2) not null
);


INSERT INTO accounts.Users
(id, name, birthDate, money)
values
('user1', 'user first', '2000-01-01', 1000),
('user2', 'user second', '1990-01-01', 2000)
;