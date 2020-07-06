INSERT INTO user (id, username, password, name, email, mobile, enabled, date_created)
VALUES (99999, 'test1', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32', 'Test User 1',
        'test1@localhost',
        '0123456789', 1, CURDATE());
INSERT INTO user_authority (authority_id, user_id)
VALUES (2, 99999);
INSERT INTO product (id, code, name, average_rating, summary, description, image, trade_price, retail_price,
                     delivery_time, available)
VALUES (99999, 'TEST-A000-00001', 'Test Product 1', 5,
        'Product Summary.', 'This is a brief description of the product for testing.', 'generic-product-1.jpg',
        10.0, 20.0, 5, 5);
INSERT INTO message (id, user_id, text, read)
values (99999, 99999, 'This is an example message', 0);

