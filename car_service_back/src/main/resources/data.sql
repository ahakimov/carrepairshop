insert into car_service.clients (name, phone, email, password)
values ('Client A', 'Phone A', 'Email A', 'Password A'),
       ('Client B', 'Phone B', 'Email B', 'Password B');

insert into car_service.cars (model, make, produced, license_plate, owner_id)
values ('Model A', 'Make A', 2000, 'Car A', 1),
       ('Model B', 'Make B', 2001, 'Car B', 2);

insert into car_service.mechanics (name, phone, email, password, specialty, experience)
values ('Mechanic A', 'Phone A', 'Email A', 'Password A', 'Specialty A', 1),
       ('Mechanic B', 'Phone B', 'Email B', 'Password B', 'Specialty B', 2);

insert into car_service.services (service_name, description, price, estimated_duration)
values ('Service A', 'Description A', 12345.00, 1),
       ('Service B', 'Description B', 54321.00, 2);

insert into car_service.reservations (contact_info, date_added, status, mechanic_id, service_id)
values ('Contact A', '2025-05-05T12:00:00', 'in progress', 1, 1),
       ('Contact B', '2025-06-06T12:00:00', 'in progress', 2, 2);
