DROP PROCEDURE IF EXISTS addToCart;

DELIMITER $$

CREATE PROCEDURE addToCart(input_orderID INT, input_customerID INT, input_shoeID INT)
add_to_cart : BEGIN
DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT('SQLEXCEPTION: OCCURED, a rollback has been done');
    END;
SET AUTOCOMMIT = 0;
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
            INSERT INTO order_item(order_id, shoe_id) VALUES(LAST_INSERT_ID(), input_shoeID);
				UPDATE shoe SET quantity_in_stock = (quantity_in_stock) - 1
				WHERE shoe_id = input_shoeID;
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
        
        
COMMIT;
SET AUTOCOMMIT = 1;

END $$

DELIMITER ;