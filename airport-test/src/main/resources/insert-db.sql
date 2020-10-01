INSERT INTO flights  (flight_id ,direction, date_flight ) VALUES (101, 'Minsk-Brest', '2020-03-11');
INSERT INTO flights  (direction, date_flight  ) VALUES ('Minsk-Frankfurt', '2020-03-12');
INSERT INTO flights  (direction, date_flight  ) VALUES ('Minsk-Brest', '2020-04-10');
INSERT INTO flights  (direction, date_flight  ) VALUES ('Minsk-Paris', '2020-04-15');
INSERT INTO flights  (direction, date_flight  ) VALUES ('Minsk-Brest', '2020-04-09');

INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Michail', 'Ivanov',101);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Nickolai', 'Kroch',105);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Ann', 'Loza',101);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Sonia', 'Govin',104);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Alex', 'Kroch',101);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Michail', 'Petrov',102);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Ann', 'Tromza',102);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Ivan', 'Kovalev',101);
INSERT INTO passengers   (firstname ,lastname ,flight_id  ) VALUES ('Nickolai', 'Loza',105);