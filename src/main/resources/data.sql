INSERT INTO authority (name, id) VALUES ('ROLE_ADMIN', 1);
INSERT INTO authority (name, id) VALUES ('ROLE_USER', 2);
INSERT INTO user (id, username, password, date_created) VALUES (1,'admin','$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',CURDATE());
INSERT INTO user (id, username, password, date_created) VALUES (2,'user','$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',CURDATE());
INSERT INTO user_authority (authority_id, user_id) VALUES (1, 1);
INSERT INTO user_authority (authority_id, user_id) VALUES (2, 1);
INSERT INTO user_authority (authority_id, user_id) VALUES (2, 2);

