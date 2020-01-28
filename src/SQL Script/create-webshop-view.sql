DROP VIEW IF EXISTS view_average_ratings;

CREATE VIEW view_average_ratings
 AS
    SELECT 
        `s`.`shoe_id` AS `ID`,
        `b`.`name` AS `Märke`,
        `s`.`model` AS `Modell`,
        CALCULATE_AVERAGE_RATING(`s`.`shoe_id`) AS `Medelbetyg`,
        (SELECT 
                `rating`.`rating`
            FROM
                `rating`
            WHERE
                (`rating`.`rating_value` = ROUND(`Medelbetyg`, 0))) AS `Betyg`
    FROM
        (((`shoe` `s`
        LEFT JOIN `review` `rw` ON ((`rw`.`shoe_id` = `s`.`shoe_id`)))
        LEFT JOIN `rating` `r` ON ((`rw`.`rating_id` = `r`.`rating_id`)))
        LEFT JOIN `brand` `b` ON ((`s`.`brand_id` = `b`.`brand_id`)))
    GROUP BY `s`.`shoe_id`;

DROP VIEW IF EXISTS view_shoe_table;

CREATE VIEW view_shoe_table
AS
    SELECT 
        `s`.`shoe_id` AS `shoe_id`,
        `category`.`type` AS `category`,
        `brand`.`name` AS `brand`,
        `s`.`model` AS `model`,
        `color`.`color` AS `color`,
        `size`.`size` AS `size`,
        `s`.`quantity_in_stock` AS `quantity_in_stock`,
        `s`.`unit_price` AS `unit_price`
    FROM
        ((((`shoe` `s`
        JOIN `size` ON ((`size`.`size_id` = `s`.`size_id`)))
        JOIN `brand` ON ((`brand`.`brand_id` = `s`.`brand_id`)))
        JOIN `color` ON ((`color`.`color_id` = `s`.`color_id`)))
        JOIN `category` ON ((`s`.`category_id` = `category`.`category_id`)))
