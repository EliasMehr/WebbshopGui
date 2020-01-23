USE sql_webshop;

SELECT
c.type AS Kategori,
       COUNT(shoe_id) AS Antal
FROM catalouge cl
JOIN category c on cl.category_id = c.category_id
GROUP BY c.type;

SELECT
    c.first_name AS Namn,
    c.last_name AS Efternamn,
    SUM(s.unit_price * oi.quantity) AS Handlat_För
FROM customer c
         LEFT JOIN orders o
                   ON o.customer_id = c.customer_id
         LEFT JOIN order_item oi
                   ON oi.order_id = o.order_id
         LEFT JOIN shoe s
                   ON s.shoe_id = oi.shoe_id
GROUP BY c.customer_id;

SELECT c.first_name AS Förnamn,
       c.last_name AS Efternamn,
       c2.type AS Kategori,
       b.name AS Märke,
       c3.color AS Färg,
       s2.size AS Storlek
FROM customer c
         JOIN orders o on c.customer_id = o.customer_id
         JOIN order_item oi on o.order_id = oi.order_id
         JOIN shoe s on oi.shoe_id = s.shoe_id
         JOIN category c2 on s.category_id = c2.category_id
         JOIN size s2 on s.size_id = s2.size_id
         JOIN brand b on s.brand_id = b.brand_id
         JOIN color c3 on s.color_id = c3.color_id
WHERE b.name LIKE 'ECCO' AND c3.color LIKE 'SVART' AND s2.size LIKE '38%';

SELECT c.city AS Ort,
       SUM(s.unit_price * oi.quantity) AS Beställningsvärde
FROM customer c
         JOIN orders o on c.customer_id = o.customer_id
         JOIN order_item oi on o.order_id = oi.order_id
         JOIN shoe s on oi.shoe_id = s.shoe_id
GROUP BY c.city
HAVING Beställningsvärde >= 1000;

SELECT b.name           AS Märke,
       s.model          AS Modell,
       s.shoe_id        AS Sko_ID,
       SUM(oi.quantity) AS Antal
FROM customer c
         JOIN orders o on c.customer_id = o.customer_id
         JOIN order_item oi on o.order_id = oi.order_id
         JOIN shoe s on oi.shoe_id = s.shoe_id
         JOIN brand b on s.brand_id = b.brand_id
GROUP BY oi.shoe_id
ORDER BY Antal DESC
LIMIT 5;

SELECT {fn MONTHNAME(o.date)} AS Månad,
       EXTRACT(YEAR FROM o.date) AS ÅR,
       SUM(s.unit_price * oi.quantity) AS Försäljning_i_SEK
FROM customer c
         JOIN orders o on c.customer_id = o.customer_id
         JOIN order_item oi on o.order_id = oi.order_id
         JOIN shoe s on oi.shoe_id = s.shoe_id
         JOIN brand b on s.brand_id = b.brand_id

GROUP BY EXTRACT(YEAR_MONTH FROM o.date);