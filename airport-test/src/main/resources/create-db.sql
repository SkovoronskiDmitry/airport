DROP TABLE IF EXISTS passengers;
DROP TABLE IF EXISTS flights;
CREATE TABLE flights (
  flight_id INT NOT NULL AUTO_INCREMENT,
  direction VARCHAR(45) NOT NULL,
  date_flight DATETIME,
  PRIMARY KEY (flight_id)
);
CREATE TABLE passengers (
  passenger_id INt NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(20) NOT NULL,
  lastname VARCHAR(30) NOT NULL,
  flight_id INTEGER NOT NULL,
  PRIMARY KEY (passenger_id),
 	FOREIGN KEY (flight_id) REFERENCES flights (flight_id) ON DELETE CASCADE);
