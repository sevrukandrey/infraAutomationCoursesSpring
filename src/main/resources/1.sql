
DROP DATABASE car_shop;
CREATE DATABASE car_shop;
USE car_shop;

CREATE TABLE `client` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`name` VARCHAR(25) DEFAULT NULL,
`surname` VARCHAR(25) DEFAULT NULL,
`phone_number` VARCHAR(25) NOT NULL,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `car` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`plate_number` VARCHAR(25) NOT NULL,
`year` INT NOT NULL,
`color` VARCHAR(20) DEFAULT NULL,
`model` VARCHAR(20) DEFAULT NULL,
`brand` VARCHAR(20) DEFAULT NULL,
PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `advert`(
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`car_id` BIGINT UNSIGNED NOT NULL,
`seller_id` BIGINT UNSIGNED NOT NULL,
`price` DOUBLE UNSIGNED NOT NULL,
`deal_id` BIGINT UNSIGNED DEFAULT NULL,
`status`  VARCHAR(25) DEFAULT 'OPEN',
PRIMARY KEY (id),
FOREIGN KEY (car_id) REFERENCES `car`(id),
FOREIGN KEY (seller_id) REFERENCES `client`(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DELIMITER $$
CREATE TRIGGER price_should_be_bigger_then_zero
  BEFORE INSERT ON advert
  FOR EACH ROW
    BEGIN
    IF NEW.price <= 0
    THEN SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Price can not be 0 or less';
   END IF;
 END;
$$
CREATE TRIGGER price_should_be_bigger_then_zero_on_update
BEFORE UPDATE ON advert
 FOR EACH ROW
  BEGIN
    IF NEW.price <= 0
    THEN SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Price can not be 0 or less';
    END IF;
  END;
 $$
 CREATE TRIGGER only_one_car_allow_to_insert_sale_in_one_time
 BEFORE INSERT ON advert
 FOR EACH ROW
   BEGIN
     IF NEW.status = 'Open' AND
        (SELECT COUNT(id) FROM `advert` WHERE STATUS='Open' AND car_id = NEW.car_id) > 0
     THEN
      SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Only one car can be in advert with status Open';
      END IF;
 END;
 $$
 CREATE TRIGGER only_one_car_allow_to_update_sale_in_one_time
 BEFORE UPDATE ON advert
 FOR EACH ROW
   BEGIN
     IF NEW.status = 'Open' AND NEW.status <> OLD.status AND
        (SELECT COUNT(id) FROM `advert` WHERE STATUS='Open' AND car_id = NEW.car_id) > 0
     THEN
      SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Only one car can be in advert with status Open';
      END IF;
 END;
 $$
DELIMITER ;

CREATE TABLE `deal` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`buyer_id` BIGINT UNSIGNED NOT NULL,
`price` DOUBLE UNSIGNED NOT NULL,
`advert_id` BIGINT UNSIGNED NOT NULL,
`status` VARCHAR(25) DEFAULT 'ACTIVE',
PRIMARY KEY (`id`),
FOREIGN KEY (buyer_id) REFERENCES `client`(id),
FOREIGN KEY (advert_id) REFERENCES `advert`(id)
) ENGINE=INNODB  DEFAULT CHARSET=utf8;

DELIMITER $$
CREATE TRIGGER price_in_deal_should_be_bigger_then_zero
  BEFORE INSERT ON deal
  FOR EACH ROW
    BEGIN
    IF NEW.price <= 0
    THEN SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Price can not be 0 or less';
   END IF;
 END;
$$
CREATE TRIGGER price_in_deal_should_be_bigger_then_zero_on_update
BEFORE UPDATE ON deal
 FOR EACH ROW
  BEGIN
    IF NEW.price <= 0
    THEN SIGNAL SQLSTATE '22003' SET MESSAGE_TEXT = 'Price can not be 0 or less';
    END IF;
  END;
 $$
DELIMITER ;

INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Andrey", "Sevruk","093-774");
INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Sasha", "Ivanov","100-589");
INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Vova", "Petrov","789-526");
INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Sasha", "Ivanov","145585");
INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Tom", "Deny","32123");
INSERT INTO `client`(NAME,surname,phone_number) VALUES ("Jack", "Tomer","14532211585");

INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("1212", 2015,"black","fiesta");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("1515", 2013,"green","x5");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("1213", 2016,"black","rio");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("1312", 2010,"black","c4");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("1234", 2013,"green","x3");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("12356", 2015,"black","golf");
INSERT INTO `car`(plate_number, YEAR, color, model) VALUES ("43221", 2003,"red","c1");

INSERT INTO `advert`(car_id, seller_id, price) VALUES (1,1,2000);
INSERT INTO `advert`(car_id, seller_id, price) VALUES (2,2,3000);
INSERT INTO `advert`(car_id, seller_id, price) VALUES (3,3,3000);
INSERT INTO `advert`(car_id, seller_id, price) VALUES (4,2,3000);
INSERT INTO `advert`(car_id, seller_id, price) VALUES (6,4,3000);