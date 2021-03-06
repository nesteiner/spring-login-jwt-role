drop table if exists role;
drop table if exists user;
drop table if exists user_roles;

create table role(
    `id` bigint not null auto_increment,
    `description` varchar(255),
    `name` varchar(255),
    primary key(`id`)
);

create table user_roles(
    `user_id` bigint not null,
    `role_id` bigint not null,
    primary key(user_id, role_id)
);

insert into role (id, description, name) values (4, 'Admin role', 'ADMIN');
insert into role (id, description, name) values (5, 'User role', 'USER');

select * from role;
select * from `user`;