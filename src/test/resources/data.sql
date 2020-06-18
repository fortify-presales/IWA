INSERT INTO user (id, username, password, name, email, mobile, enabled, date_created)
    VALUES (3, 'test', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32', 'test user 1', 'test1@localhost',
            '0123456789', 1, CURDATE());
INSERT INTO user_authority (authority_id, user_id)
    VALUES (2, 3);
