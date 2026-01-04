create schema if not exists car_service;

create table if not exists car_service.clients (
    id int PRIMARY KEY NOT NULL auto_increment,
    name varchar,
    phone varchar,
    email varchar,
    password varchar
);

create table if not exists car_service.cars (
    id int PRIMARY KEY NOT NULL auto_increment,
    model varchar,
    make varchar,
    produced int,
    license_plate varchar,
    owner_id long,
    foreign key (owner_id) references clients(id)
);

create table if not exists car_service.mechanics (
    id int PRIMARY KEY NOT NULL auto_increment,
    name varchar,
    phone varchar,
    email varchar,
    password varchar,
    specialty varchar,
    experience int
);


create table if not exists car_service.services (
    id int PRIMARY KEY NOT NULL auto_increment,
    service_name varchar,
    description varchar,
    price double,
    estimated_duration long
);

create table if not exists car_service.reservations (
    id int PRIMARY KEY NOT NULL auto_increment,
    contact_info varchar,
    date_added datetime,
    status varchar,
    mechanic_id long,
    service_id long,
    foreign key (mechanic_id) references mechanics(id),
    foreign key (service_id) references services(id)
);

create table if not exists car_service.reservations (
    id int PRIMARY KEY NOT NULL auto_increment,
    client_id long,
    mechanic_id long,
    start_date_time datetime,
    end_date_time datetime,
    service_id long,
    status varchar,
    additional_details varchar,
    foreign key (client_id) references clients(id),
    foreign key (mechanic_id) references mechanics(id),
    foreign key (service_id) references services(id)
);