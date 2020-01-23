DROP FUNCTION IF EXISTS validate_if_current_order_exists;

DELIMITER $$

CREATE FUNCTION validate_if_current_order_exists(input_orderID INT)
RETURNS BOOLEAN
BEGIN
DECLARE check_orderExistance BOOLEAN;
SELECT COUNT(*) INTO check_orderExistance
FROM orders WHERE order_id = input_orderID;
RETURN check_orderExistance;
END $$

DELIMITER ;

DROP FUNCTION IF EXISTS validate_if_customer_exists;

DELIMITER $$

CREATE FUNCTION validate_if_customer_exists(input_customerID INT)
RETURNS BOOLEAN
BEGIN
DECLARE check_customerExistance BOOLEAN;
SELECT COUNT(*) INTO check_customerExistance
FROM customer WHERE customer_id = input_customerID;
RETURN check_customerExistance;
END $$

DELIMITER ;

DROP FUNCTION IF EXISTS validate_if_shoe_exists;

DELIMITER $$

CREATE FUNCTION validate_if_shoe_exists(input_shoeID INT)
RETURNS BOOLEAN
BEGIN
DECLARE check_shoeExistance BOOLEAN;
SELECT COUNT(*) INTO check_shoeExistance
FROM shoe WHERE shoe_id = input_shoeID;
RETURN check_shoeExistance;
END $$

DELIMITER ;


DROP FUNCTION IF EXISTS validate_if_shoe_exists_in_current_order;

DELIMITER $$

CREATE FUNCTION validate_if_shoe_exists_in_current_order(input_orderID INT ,input_shoeID INT)
RETURNS BOOLEAN
BEGIN
DECLARE check_shoeExistance BOOLEAN;
SELECT COUNT(*) INTO check_shoeExistance
FROM order_item WHERE shoe_id = input_shoeID AND order_id = input_orderID;
RETURN check_shoeExistance;
END $$

DELIMITER ;


DROP FUNCTION IF EXISTS calculate_average_rating;

DELIMITER $$
CREATE FUNCTION calculate_average_rating(shoeID INT)
RETURNS DOUBLE
BEGIN
	DECLARE average_score DOUBLE;
	SELECT SUM(r.rating_value) / COUNT(rw.shoe_id) INTO average_score
    FROM review rw
    JOIN rating r ON rw.rating_id = r.rating_id
    GROUP BY rw.shoe_id
    HAVING rw.shoe_id = shoeID;
    RETURN average_score;
END $$

DELIMITER ;
