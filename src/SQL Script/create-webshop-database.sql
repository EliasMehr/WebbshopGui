-- DROP Database if it already EXISTS

DROP DATABASE IF EXISTS  sql_webshop;
CREATE DATABASE sql_webshop;

-- Selecting proper schema to use and setting default timezone
USE sql_webshop;
SET GLOBAL TIME_ZONE = '+1:00';

-- Creating relevant tables for database
CREATE TABLE category (
    category_id INT NOT NULL AUTO_INCREMENT,
    type VARCHAR(55) NOT NULL,
    PRIMARY KEY(category_id)
);

CREATE TABLE brand (
    brand_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(55) NOT NULL,
    PRIMARY KEY(brand_id)
);

CREATE TABLE color (
    color_id INT NOT NULL AUTO_INCREMENT,
    color VARCHAR(55) NOT NULL,
    PRIMARY KEY (color_id)
);

CREATE TABLE size (
    size_id INT NOT NULL AUTO_INCREMENT,
    size VARCHAR(55) NOT NULL,
    PRIMARY KEY (size_id)
);

CREATE TABLE shoe (
    shoe_id INT NOT NULL AUTO_INCREMENT,
    category_id INT,
    brand_id INT,
    model VARCHAR(55) NOT NULL,
    color_id INT,
    size_id INT,
    quantity_in_stock INT NOT NULL,
    unit_price INT NOT NULL,
    PRIMARY KEY (shoe_id),

    FOREIGN KEY(category_id) REFERENCES category(category_id) ON DELETE SET NULL,
    FOREIGN KEY(brand_id) REFERENCES brand(brand_id) ON DELETE SET NULL,
    FOREIGN KEY(color_id) REFERENCES color(color_id) ON DELETE SET NULL,
    FOREIGN KEY(size_id) REFERENCES size(size_id) ON DELETE SET NULL
);

CREATE TABLE customer (
    customer_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    person_id VARCHAR(50) NOT NULL,
    password VARCHAR(50) COLLATE Latin1_general_cs, 
    PRIMARY KEY (customer_id)
);

CREATE TABLE orders (
    order_id INT NOT NULL AUTO_INCREMENT,
    customer_id INT,
    date DATE NOT NULL,
    PRIMARY KEY (order_id),

    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE SET NULL
);

CREATE TABLE order_item (
    order_itemID INT NOT NULL AUTO_INCREMENT,
    order_id INT,
    shoe_id INT,
    quantity INT NOT NULL DEFAULT 1 CHECK(quantity >=1),
    PRIMARY KEY (order_itemID),
    UNIQUE KEY (order_id, shoe_id),

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (shoe_id) REFERENCES shoe(shoe_id) ON DELETE SET NULL
);

CREATE TABLE rating (
        rating_id INT NOT NULL AUTO_INCREMENT,
        rating VARCHAR (50) NOT NULL,
	rating_value INT NOT NULL,
        PRIMARY KEY (rating_id)
);

CREATE TABLE review (
    review_id INT NOT NULL AUTO_INCREMENT,
    customer_id INT NOT NULL,
    shoe_id INT NOT NULL,
    comments VARCHAR(255) NOT NULL,
    rating_id INT NOT NULL,
    PRIMARY KEY (review_id),
    UNIQUE KEY (customer_id, shoe_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (shoe_id) REFERENCES shoe(shoe_id),
    FOREIGN KEY (rating_id) REFERENCES rating(rating_id)
);

CREATE TABLE catalouge (
    catalouge_id INT NOT NULL AUTO_INCREMENT,
    category_id INT NOT NULL,
    shoe_id INT NOT NULL,
    PRIMARY KEY (catalouge_id),
    UNIQUE KEY (shoe_id, category_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (shoe_id) REFERENCES shoe(shoe_id)
);

CREATE TABLE not_in_stock (
stocktake_id INT NOT NULL AUTO_INCREMENT,
shoe_id INT NOT NULL,
out_of_stock_date DATE NOT NULL,
PRIMARY KEY (stocktake_id),

FOREIGN KEY (shoe_id) REFERENCES shoe(shoe_id) ON DELETE CASCADE
);

-- En databas på denna nivå kommmer detta att inte vara till någon nytta men en databas som innehåller miljoner poster så
-- kommer INDEX att underlätta att effektivisera sökningen då man kollar efter angivna VALUES man specifikt leta igenom!

CREATE INDEX IX_shoe_models ON shoe(model);
CREATE INDEX IX_customer_first_and_last_name ON customer(first_name, last_name);

-- ## Populating the tables with DATA

-- Populating #Category table
INSERT INTO category VALUES(1, 'Sneakers');
INSERT INTO category VALUES(2, 'Sandaler');
INSERT INTO category VALUES(3, 'Boots');
INSERT INTO category VALUES(4, 'Sportskor');
INSERT INTO category VALUES(5, 'Vandringsskor');

-- Populating #Color table
INSERT INTO color VALUES(1, 'SVART');
INSERT INTO color VALUES(2, 'VIT');
INSERT INTO color VALUES(3, 'BRUN');
INSERT INTO color VALUES(4, 'BLÅ');

-- Populating #Brand table
INSERT INTO brand VALUES(1, 'NIKE');
INSERT INTO brand VALUES(2, 'ADIDAS');
INSERT INTO brand VALUES(3, 'PUMA');
INSERT INTO brand VALUES(4, 'DR MARTENS');
INSERT INTO brand VALUES(5, 'ECCO');
INSERT INTO brand VALUES(6, 'NORTH FACE');

-- Populating #Size table
INSERT INTO size VALUES (1, '38 EU');
INSERT INTO size VALUES (2, '39 EU');
INSERT INTO size VALUES (3, '40 EU');
INSERT INTO size VALUES (4, '41 EU');
INSERT INTO size VALUES (5, '42 EU');
INSERT INTO size VALUES (6, '43 EU');
INSERT INTO size VALUES (7, 'ONE SIZE');


-- Populating #Shoe table
INSERT INTO shoe VALUES(1, 1, 1, 'AIR MAX LO-FIT', 1, 3, 2, 1190);
INSERT INTO shoe VALUES(2, 1, 1, 'AIR MAX LO-FIT', 2, 3, 2, 1190);
INSERT INTO shoe VALUES(3, 1, 1, 'FLY KNIT', 1, 3, 2, 2290);
INSERT INTO shoe VALUES(4, 1, 1, 'FLY KNIT', 2, 3, 2, 2290);

INSERT INTO shoe VALUES(5, 1, 2, 'ULTRA BOOST UNCAGED', 1, 4, 2, 2390);
INSERT INTO shoe VALUES(6, 1, 2, 'ULTRA BOOST UNCAGED', 2, 4, 2, 2390);
INSERT INTO shoe VALUES(7, 1, 2, 'ORIGINALS STAN SMITH', 1, 4, 2, 799);
INSERT INTO shoe VALUES(8, 1, 2, 'ORIGINALS STAN SMITH', 2, 4, 2, 799);

INSERT INTO shoe VALUES(9, 1, 3, 'COURT STAR', 1, 4, 2, 399);
INSERT INTO shoe VALUES(10, 1, 3, 'COURT STAR', 2, 4, 2, 399);
INSERT INTO shoe VALUES(11, 1, 3, 'COURT BREAKER', 1, 4, 2, 299);
INSERT INTO shoe VALUES(12, 1, 3, 'COURT BREAKER', 2, 4, 2, 299);

-- SANDALS
INSERT INTO shoe VALUES(13, 2, 1, 'DRY GRIP', 1, 7, 2, 399);
INSERT INTO shoe VALUES(14, 2, 1, 'DRY GRIP', 2, 7, 2, 399);
INSERT INTO shoe VALUES(15, 2, 2, 'ULTRAS SAN', 1, 7, 2, 399);
INSERT INTO shoe VALUES(16, 2, 2, 'ULTRAS SAN', 2, 7, 2, 399);
INSERT INTO shoe VALUES(17, 2, 2, 'OCEAN D', 1, 7, 2, 399);
INSERT INTO shoe VALUES(18, 2, 2, 'OCEAN D', 2, 7, 2, 399);
INSERT INTO shoe VALUES(19, 2, 5, 'LEATHER SANDALS', 1, 1, 2, 599);
INSERT INTO shoe VALUES(20, 2, 5, 'LEATHER SANDALS', 2, 1, 2, 599);

-- BOOTS
INSERT INTO shoe VALUES(21, 3, 4, 'HI-RUGGED LEATHER', 1, 4, 2, 1490);
INSERT INTO shoe VALUES(22, 3, 4, 'HI-RUGGED LEATHER', 3, 4, 2, 1490);

-- SPORT SHOES
INSERT INTO shoe VALUES(23, 4, 1, 'MERCURIAL RADAR', 1, 5, 2, 1390);
INSERT INTO shoe VALUES(24, 4, 1, 'STEALTH OPS', 1, 5, 2, 1690);
INSERT INTO shoe VALUES(25, 4, 1, 'NUOVO PHANTOM', 1, 5, 2, 690);

-- HIKING SHOES
INSERT INTO shoe VALUES(26, 5, 6, 'BACK TO BERKELEY', 1, 6, 2, 999);

-- Populating #Customer table
INSERT INTO customer VALUES(1, 'Albin', 'Ekdahl', 'Mjölnaren 3', 'Järfälla', '17741', '0734574456', 'albin.ekdahl@gmail.com', '890101-6640', null);
INSERT INTO customer VALUES(2, 'Robin', 'Andersson', 'Hättkvarnsgatan 5', 'Järfälla', '17741', '0734574333', 'robin.andersson@gmail.com', '790101-2440', null);
INSERT INTO customer VALUES(3, 'Elsa', 'Nyström', 'Kungsgatan 35', 'Stockholm', '12120', '0704582456', 'elsa.nyström@gmail.com', '960101-1095', null);
INSERT INTO customer VALUES(4, 'Sofia', 'Östhammar', 'Vasavägen 5', 'Täby', '18640', '0764123456', 'sofia.osthammar@gmail.com', '540320-2619', null);
INSERT INTO customer VALUES(5, 'Elias', 'Mehr', 'Jungfrudansen', 'Solna', '17150', '0735856673', 'elias.mehr@gmail.com', '890116-9999', null);
INSERT INTO customer VALUES(6, 'Adam', 'Boyaci', 'Ekedahlsvägen 5', 'Uppands-Bro', '14465', '0700400200', 'Adam.boyaci@gmail.com', '750620-3340', 'Halloj12!');


-- Populating #Rating table
INSERT INTO rating VALUES (1, 'MISSNÖJD', 1 );
INSERT INTO rating VALUES (2, 'GANSKA NÖJD', 2);
INSERT INTO rating VALUES (3, 'NÖJD', 3);
INSERT INTO rating VALUES (4, 'VÄLDIGT NÖJD', 4);


-- Populating #Orders table
INSERT INTO orders VALUES (1, 1, '2020-01-05');
INSERT INTO orders VALUES (2, 1, '2020-01-05');
INSERT INTO orders VALUES (3, 3, '2020-02-10');
INSERT INTO orders VALUES (4, 4, '2020-01-30');
INSERT INTO orders VALUES (5, 5, '2019-12-16');
INSERT INTO orders VALUES (6, 4, '2019-10-20');
INSERT INTO orders VALUES (7, 2, '2019-11-16');

-- Populating #Order_item table
INSERT INTO order_item VALUES (1, 1, 1, 1);
INSERT INTO order_item VALUES (2, 1, 3, 1);
INSERT INTO order_item VALUES (3, 3, 19, 2);
INSERT INTO order_item VALUES (4, 4, 19, 1);
INSERT INTO order_item VALUES (5, 5, 20, 1);
INSERT INTO order_item VALUES (6, 4, 8, 1);
INSERT INTO order_item VALUES (7, 7, 4, 2);

-- Populating #Catalouge table
INSERT INTO catalouge VALUES (1, 1, 1);
INSERT INTO catalouge VALUES (2, 2, 14);
INSERT INTO catalouge VALUES (3, 4, 8);
INSERT INTO catalouge VALUES (4, 5, 20);
INSERT INTO catalouge VALUES (5, 4, 25);

-- Populating #Review table
INSERT INTO review VALUES (1, 1, 1, 'Väldigt snabb leverans, skorna är väldigt bekväma', 4);
INSERT INTO review VALUES (2, 1, 3, 'Väldigt snabb leverans, skorna är väldigt bekväma', 4);
INSERT INTO review VALUES (3, 3, 19, 'Sandalerna är väldigt bekväma och mjuka och snygga i läder', 3);
INSERT INTO review VALUES (4, 4, 19, 'Sandalerna är riktigt snygga, mindre bekväma men hanterbart', 3);
