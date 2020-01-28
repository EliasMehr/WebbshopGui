DROP TRIGGER IF EXISTS update_stocktake;

DELIMITER $$

CREATE TRIGGER update_stocktake
AFTER UPDATE ON shoe
FOR EACH ROW
BEGIN
	IF (new.quantity_in_stock = 0) THEN
    INSERT INTO not_in_stock(shoe_id, out_of_stock_date)
    VALUES(old.shoe_id, CURRENT_DATE());
END IF;

END $$

DELIMITER ;