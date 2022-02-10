INSERT INTO authorities (name, id)
values ('ROLE_TEST', '92f90350-e795-4019-812a-afd23300343f');
INSERT INTO users (id, username, password, first_name, last_name, email, phone, address, city, state, zip, country, enabled, date_created)
VALUES ('bd5b9e2f-ac55-4e34-a76d-599b7e5b3308', 'test1', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',
        'Test', 'User 1',
        'test1@localhost',
        '0123456789',
        '1 Somewhere Street', 'London', 'Greater London', 'SW1', 'United Kingdom',
        1, CURDATE());
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('92f90350-e795-4019-812a-afd23300343f', 'bd5b9e2f-ac55-4e34-a76d-599b7e5b3308');
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock,
                      time_to_stock, available)
VALUES ('38400000-8cf0-11bd-b23e-10b96e4ef00d', 'TEST-A000-00001', 'Test Product 1', 5,
        'Product Summary.', 'This is a brief description of the product for testing.', 'generic-product-1.jpg',
        10.0, 1, 5, 5);
INSERT INTO messages (id, user_id, text, read)
VALUES ('d67dbf99-e775-4d1b-87f7-0b86c739a5ba', 'bd5b9e2f-ac55-4e34-a76d-599b7e5b3308', 'This is an example message',
        0);
INSERT INTO orders (id, user_id, order_num, amount, shipped, cart)
VALUES ('c97f4c8b-4c14-4d0c-9354-69fd7ee324da', 'bd5b9e2f-ac55-4e34-a76d-599b7e5b3308', 'TEST-O000-0001', 100.0, 0,
        '[{"id":"6bbbeb10-6709-4163-a790-f691b09d6aca","name":"Dontax","price":8.50,"quantity":4}]');
INSERT INTO reviews (id, product_id, user_id, review_date, comment, rating, visible)
VALUES ('8ab3eb7c-42f6-11ec-81d3-0242ac130003', '38400000-8cf0-11bd-b23e-10b96e4ef00d', 'bd5b9e2f-ac55-4e34-a76d-599b7e5b3308',
    CURDATE()-5, 'This is an example review of Test Product 1. It is very good', 5, 1);

