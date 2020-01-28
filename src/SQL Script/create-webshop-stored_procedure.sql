DROP PROCEDURE IF EXISTS addToCart;

DELIMITER $$

CREATE PROCEDURE addToCart (input_orderID INT, input_customerID INT, input_shoeID INT)
add_to_cart : BEGIN
DECLARE newOrderID INT;
DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT('SQLEXCEPTION: OCCURED, a rollback has been done') AS ERROR_MESSAGE;
    END;
START TRANSACTION;
	ROLLBACK;
		IF NOT validate_if_customer_exists(input_customerID) THEN 
			SELECT('MESSAGE: Customer does not exist') AS ERROR_MESSAGE;
            LEAVE add_to_cart;
            
		ELSEIF NOT validate_if_shoe_exists(input_shoeID) THEN
			SELECT('MESSAGE: Shoe does not exist') AS ERROR_MESSAGE;
            LEAVE add_to_cart;
		END IF;
        
		IF NOT validate_if_current_order_exists(input_orderID) THEN
			INSERT INTO orders(customer_id, date) VALUES(input_customerID, CURRENT_DATE());
            SET newOrderID = LAST_INSERT_ID();
            INSERT INTO order_item(order_id, shoe_id) VALUES(newOrderID, input_shoeID);
				UPDATE shoe SET quantity_in_stock = (quantity_in_stock) - 1
				WHERE shoe_id = input_shoeID;
                SELECT(newOrderID) AS newOrderID;
			COMMIT;
            LEAVE add_to_cart;
        END IF;
        
        IF validate_if_shoe_exists_in_current_order(input_orderID, input_shoeID) THEN
			UPDATE order_item SET quantity = (quantity) + 1 
            WHERE order_id = input_orderID AND shoe_id = input_shoeID;
		ELSE
			INSERT INTO order_item(order_id, shoe_id, quantity) VALUES(input_orderID, input_shoeID, 1);
        END IF;
        
		UPDATE shoe SET quantity_in_stock = (quantity_in_stock) - 1
        WHERE shoe_id = input_shoeID;
        SELECT('Successfully added');
COMMIT;
END $$

DELIMITER ;


DROP PROCEDURE IF EXISTS rate_product;

DELIMITER $$

CREATE PROCEDURE rate_product(customerID INT, shoeID INT, commentField VARCHAR(255), ratingID INT)
BEGIN
	IF NOT validate_if_customer_exists(customerID) THEN
		SELECT('MESSAGE: Customer does not exist') AS ERROR_MESSAGE;
        
	ELSEIF NOT validate_if_shoe_exists(shoeID) THEN
		SELECT('MESSAGE: Shoe does not exist') AS ERROR_MESSAGE;
	
    ELSEIF NOT validate_if_rating_exists(ratingID) THEN
		SELECT('MESSAGE: Rating does not exist') AS ERROR_MESSAGE;
	ELSE
		INSERT INTO review(customer_id, shoe_id, comments, rating_id) VALUES(customerID, shoeID, commentField, ratingID);
        END IF;
END $$

DELIMITER ;