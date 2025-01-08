create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);



insert into `customer` (email, pwd, role) values ('happy@example.com', '{noop}1234', 'read');
insert into `customer`(email, pwd, role) values ('admin@example.com', '{bcrypt}$2a$12$YFTm6FeX0maaYcMGQANS3.jyxp9Ym0kMW9gSF4URFQXIgW3w.0hFq', 'admin');
