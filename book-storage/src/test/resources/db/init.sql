CREATE SCHEMA IF NOT EXISTS app_schema;

CREATE TABLE IF NOT EXISTS app_schema.categories
(
    id bigint not null primary key,
    name varchar(255)
    );

INSERT INTO categories(id, name) VALUES ('165', 'Category1');
INSERT INTO categories(id, name) VALUES ('542', 'Category2');

CREATE TABLE IF NOT EXISTS app_schema.books
(
    id bigint not null primary key,
    name varchar(255),
    author varchar(255),
    category_id bigint
);

INSERT INTO books(id, name, author, category_id) VALUES ('1534', 'Book1', 'Author1', '165');
INSERT INTO books(id, name, author, category_id) VALUES ('2213', 'Book2', 'Author2', '542');
INSERT INTO books(id, name, author, category_id) VALUES ('32345', 'Book3', 'Author3', '542');