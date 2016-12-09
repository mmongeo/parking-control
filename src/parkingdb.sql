CREATE DATABASE parking_lot;
CREATE USER 'paking_app'@'localhost' IDENTIFIED BY 'parqueo1';
USE parking_lot;
CREATE TABLE price(vehicle_type VARCHAR(20) NOT NULL, time_of_day VARCHAR(5) NOT NULL, time_amount TIME, price INT NOT NULL, PRIMARY KEY (vehicle_type, time_of_day, time_amount));
CREATE TABLE current_vehicle( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, plate VARCHAR(12), entry_time DATETIME, vehicle_type VARCHAR(20));
CREATE TABLE vehicle_history(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, plate VARCHAR(12), entry_time DATETIME, departure_time DATETIME, charge INT, vehicle_type VARCHAR(20));
GRANT ALL PRIVILEGES ON parking_lot.* to parking_app@localhost IDENTIFIED BY 'parqueo1';
