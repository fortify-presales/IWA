INSERT INTO authorities (name, id)
VALUES ('ROLE_ADMIN', '05970e74-c82b-4e21-b100-f8184d6e3454');
INSERT INTO authorities (name, id)
VALUES ('ROLE_USER', '6bdd6188-d659-4390-8d37-8f090d2ed69a');
INSERT INTO authorities (name, id)
values ('ROLE_API', 'dfc1d81b-4a7e-4248-80f7-8445ee5cb68e');
INSERT INTO users (id, username, password, first_name, last_name, email, phone, address, city, state, zip, country, date_created, enabled)
VALUES ('e18c8bcc-935d-444d-a194-3a32a3b35a49', 'admin', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',
        'Admin', 'User', 'admin@localhost.com', '0123456789', '', '', '', '', 'United Kingdom', CURDATE(), 1);
INSERT INTO users (id, username, password, first_name, last_name, email, phone, address, city, state, zip, country, date_created, enabled)
VALUES ('32e7db01-86bc-4687-9ecb-d79b265ac14f', 'user1', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',
        'Sam', 'Shopper', 'user1@localhost.com', '0123456789', '1 Somewhere Street', 'London', 'Greater London', 'SW1', 'United Kingdom', CURDATE(), 1);
INSERT INTO users (id, username, password, first_name, last_name, email, phone, address, city, state, zip, country, date_created, enabled)
VALUES ('db4cfab1-ff1d-4bca-a662-394771841383', 'user2', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',
        'Sarah', 'Shopper', 'user2@localhost.com', '0123456789', '1 Somewhere Street', 'London', 'Greater London', 'SW1', 'United Kingdom', CURDATE(), 1);
INSERT INTO users (id, username, password, first_name, last_name, email, phone, address, city, state, zip, country, date_created, enabled)
VALUES ('92a82f45-7a03-42f3-80f8-ce4e9892409d', 'api', '$2a$10$YFhTnHpCL.Z0Ev0j1CbEUub7sIWmN7Qd5RmnU8g5ekuoapV7Zdx32',
        'Api', 'User', 'api@localhost.com', '0123456789', '1 Somewhere Street', 'London', 'Greater London', 'SW1', 'United Kingdom', CURDATE(), 1);
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('05970e74-c82b-4e21-b100-f8184d6e3454', 'e18c8bcc-935d-444d-a194-3a32a3b35a49');
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('6bdd6188-d659-4390-8d37-8f090d2ed69a', 'e18c8bcc-935d-444d-a194-3a32a3b35a49');
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('dfc1d81b-4a7e-4248-80f7-8445ee5cb68e', 'e18c8bcc-935d-444d-a194-3a32a3b35a49');
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('6bdd6188-d659-4390-8d37-8f090d2ed69a', '32e7db01-86bc-4687-9ecb-d79b265ac14f');
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('6bdd6188-d659-4390-8d37-8f090d2ed69a', 'db4cfab1-ff1d-4bca-a662-394771841383');
INSERT INTO user_authorities (authority_id, user_id)
VALUES ('dfc1d81b-4a7e-4248-80f7-8445ee5cb68e', '92a82f45-7a03-42f3-80f8-ce4e9892409d');
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('eec467c8-5de9-4c7c-8541-7b31614d31a0', 'SWA234-A568-00010', 'Solodox 750', 4,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin pharetra enim erat, sed tempor mauris viverra in. Donec ante diam, rhoncus dapibus efficitur ut, sagittis a elit. Integer non ante felis. Curabitur nec lectus ut velit bibendum euismod. Nulla mattis convallis neque ac euismod. Ut vel mattis lorem, nec tempus nibh. Vivamus tincidunt enim a risus placerat viverra. Curabitur diam sapien, posuere dignissim accumsan sed, tempus sit amet diam. Aliquam tincidunt vitae quam non rutrum. Nunc id sollicitudin neque, at posuere metus. Sed interdum ex erat, et ornare purus bibendum id. Suspendisse sagittis est dui. Donec vestibulum elit at arcu feugiat porttitor.',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin pharetra enim erat, sed tempor mauris viverra in. Donec ante diam, rhoncus dapibus efficitur ut, sagittis a elit. Integer non ante felis. Curabitur nec lectus ut velit bibendum euismod. Nulla mattis convallis neque ac euismod. Ut vel mattis lorem, nec tempus nibh. Vivamus tincidunt enim a risus placerat viverra. Curabitur diam sapien, posuere dignissim accumsan sed, tempus sit amet diam. Aliquam tincidunt vitae quam non rutrum. Nunc id sollicitudin neque, at posuere metus. Sed interdum ex erat, et ornare purus bibendum id. Suspendisse sagittis est dui. Donec vestibulum elit at arcu feugiat porttitor.',
        'generic-product-4.jpg',
        12.95, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, on_sale, sale_price, in_stock, time_to_stock, available)
VALUES ('74b87e87-0d77-422c-baaa-622498a84328', 'SWA534-F528-00115', 'Alphadex Plus', 5,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit amet quam eget neque vestibulum tincidunt vitae vitae augue. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer rhoncus varius sem non luctus. Etiam tincidunt et leo non tempus. Etiam imperdiet elit arcu, a fermentum arcu commodo vel. Fusce vel consequat erat. Curabitur non lacus velit. Donec dignissim velit et sollicitudin pulvinar.',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit amet quam eget neque vestibulum tincidunt vitae vitae augue. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer rhoncus varius sem non luctus. Etiam tincidunt et leo non tempus. Etiam imperdiet elit arcu, a fermentum arcu commodo vel. Fusce vel consequat erat. Curabitur non lacus velit. Donec dignissim velit et sollicitudin pulvinar.',
        'generic-product-1.jpg',
        14.95, 1, 9.95, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('6bbbeb10-6709-4163-a790-f691b09d6aca', 'SWA179-G243-00101', 'Dontax', 3,
        'Aenean sit amet pulvinar mauris. Suspendisse eu ligula malesuada, condimentum tortor rutrum, rutrum dui. Sed vehicula augue sit amet placerat bibendum. Maecenas ac odio libero. Donec mi neque, convallis ut nulla quis, malesuada convallis velit. Aenean a augue blandit, viverra massa nec, laoreet quam. In lacinia eros quis lacus dictum pharetra.',
        'Aenean sit amet pulvinar mauris. Suspendisse eu ligula malesuada, condimentum tortor rutrum, rutrum dui. Sed vehicula augue sit amet placerat bibendum. Maecenas ac odio libero. Donec mi neque, convallis ut nulla quis, malesuada convallis velit. Aenean a augue blandit, viverra massa nec, laoreet quam. In lacinia eros quis lacus dictum pharetra.',
        'generic-product-2.jpg',
        8.50, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, on_sale, sale_price, in_stock, time_to_stock, available)
VALUES ('b6a2c319-1d14-424b-9a60-ec3ba97d21e7', 'SWA201-D342-00132', 'Tranix Life', 5,
        'Curabitur imperdiet lacus nec lacus feugiat varius. Integer hendrerit erat orci, eget varius urna varius ac. Nulla fringilla, felis eget cursus imperdiet, odio eros tincidunt est, non blandit enim ante nec magna. Suspendisse in justo maximus nisi molestie bibendum. Fusce consequat accumsan nulla, vel pharetra nulla consequat sit amet.',
        'Curabitur imperdiet lacus nec lacus feugiat varius. Integer hendrerit erat orci, eget varius urna varius ac. Nulla fringilla, felis eget cursus imperdiet, odio eros tincidunt est, non blandit enim ante nec magna. Suspendisse in justo maximus nisi molestie bibendum. Fusce consequat accumsan nulla, vel pharetra nulla consequat sit amet.',
        'generic-product-3.jpg',
        7.95, 1, 4.95, 1, 14, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('96018e5d-f34b-4e92-955c-d077809344ab', 'SWA312-F432-00134', 'Salex Two', 5,
        'In porta viverra condimentum. Morbi nibh magna, suscipit sit amet urna sed, euismod consectetur eros. Donec egestas, elit ut commodo fringilla, sem quam suscipit lectus, id tempus enim sem quis risus. Curabitur eleifend bibendum magna, vel iaculis elit varius et. Sed mollis dolor quis metus lacinia posuere. Phasellus odio mi, tempus quis dui et, consectetur iaculis odio. Quisque fringilla viverra eleifend. Cras dignissim euismod tortor, eget congue turpis fringilla sit amet. Aenean sed semper dolor, sed ultrices felis.',
        'In porta viverra condimentum. Morbi nibh magna, suscipit sit amet urna sed, euismod consectetur eros. Donec egestas, elit ut commodo fringilla, sem quam suscipit lectus, id tempus enim sem quis risus. Curabitur eleifend bibendum magna, vel iaculis elit varius et. Sed mollis dolor quis metus lacinia posuere. Phasellus odio mi, tempus quis dui et, consectetur iaculis odio. Quisque fringilla viverra eleifend. Cras dignissim euismod tortor, eget congue turpis fringilla sit amet. Aenean sed semper dolor, sed ultrices felis.',
        'generic-product-5.jpg',
        11.95, 0, 14, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, on_sale, sale_price, in_stock, time_to_stock, available)
VALUES ('b85c1e4b-3ab8-4d15-b884-24db5e246058', 'SWA654-F106-00412', 'Betala Lite', 5,
        'Sed bibendum metus vitae suscipit mattis. Mauris turpis purus, sodales a egestas vel, tincidunt ac ipsum. Donec in sapien et quam varius dignissim. Phasellus eros sem, facilisis quis vehicula sed, ornare eget odio. Nam tincidunt urna mauris, id tincidunt risus posuere ac. Integer vel est vel enim convallis blandit sed sed urna. Nam dapibus erat nunc, id euismod diam pulvinar id. Fusce a felis justo.',
        'Sed bibendum metus vitae suscipit mattis. Mauris turpis purus, sodales a egestas vel, tincidunt ac ipsum. Donec in sapien et quam varius dignissim. Phasellus eros sem, facilisis quis vehicula sed, ornare eget odio. Nam tincidunt urna mauris, id tincidunt risus posuere ac. Integer vel est vel enim convallis blandit sed sed urna. Nam dapibus erat nunc, id euismod diam pulvinar id. Fusce a felis justo.',
        'generic-product-4.jpg',
        11.95, 1, 9.95, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('6709d692-4b37-459b-ba40-3bcc3186ca09', 'SWA254-A971-00213', 'Stimlab Mitre', 5,
        'Phasellus malesuada pulvinar justo, ac eleifend magna lacinia eget. Proin vulputate nec odio at volutpat. Duis non suscipit arcu. Nam et arcu vehicula, sollicitudin eros non, scelerisque diam. Phasellus sagittis pretium tristique. Vestibulum sit amet lectus nisl. Aliquam aliquet dolor sit amet neque placerat, vel varius metus molestie. Fusce sed ipsum blandit, efficitur est vitae, scelerisque enim. Integer porttitor est et dictum blandit. Quisque gravida tempus orci nec finibus.',
        'Phasellus malesuada pulvinar justo, ac eleifend magna lacinia eget. Proin vulputate nec odio at volutpat. Duis non suscipit arcu. Nam et arcu vehicula, sollicitudin eros non, scelerisque diam. Phasellus sagittis pretium tristique. Vestibulum sit amet lectus nisl. Aliquam aliquet dolor sit amet neque placerat, vel varius metus molestie. Fusce sed ipsum blandit, efficitur est vitae, scelerisque enim. Integer porttitor est et dictum blandit. Quisque gravida tempus orci nec finibus.',
        'generic-product-6.jpg',
        12.95, 0, 7, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('ba802760-b33e-4352-acfa-0a10859b519a', 'SWA754-B418-00315', 'Alphadex Lite', 2,
        'Nam bibendum porta metus. Aliquam viverra pulvinar velit et condimentum. Pellentesque quis purus libero. Fusce hendrerit tortor sed nulla lobortis commodo. Donec ultrices mi et sollicitudin aliquam. Phasellus rhoncus commodo odio quis faucibus. Nullam interdum mi non egestas pellentesque. Duis nec porta leo, eu placerat tellus.',
        'Nam bibendum porta metus. Aliquam viverra pulvinar velit et condimentum. Pellentesque quis purus libero. Fusce hendrerit tortor sed nulla lobortis commodo. Donec ultrices mi et sollicitudin aliquam. Phasellus rhoncus commodo odio quis faucibus. Nullam interdum mi non egestas pellentesque. Duis nec porta leo, eu placerat tellus.',
        'generic-product-7.jpg', 9.95, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('339311c3-8325-464a-8ca6-4b78716f00d0', 'SWA432-E901-00126', 'Villacore 2000', 1,
        'Aliquam erat volutpat. Ut gravida scelerisque purus a sagittis. Nullam pellentesque arcu sed risus dignissim scelerisque. Maecenas vel elit pretium, ultrices augue ac, interdum libero. Suspendisse potenti. In felis metus, mattis quis lorem congue, condimentum volutpat felis. Nullam mauris mi, bibendum in ultrices sed, blandit congue ipsum.',
        'Aliquam erat volutpat. Ut gravida scelerisque purus a sagittis. Nullam pellentesque arcu sed risus dignissim scelerisque. Maecenas vel elit pretium, ultrices augue ac, interdum libero. Suspendisse potenti. In felis metus, mattis quis lorem congue, condimentum volutpat felis. Nullam mauris mi, bibendum in ultrices sed, blandit congue ipsum.',
        'generic-product-8.jpg',
        19.95, 1, 30, 1);
INSERT INTO products (id, code, name, rating, summary, description, image, price, in_stock, time_to_stock, available)
VALUES ('0bf8ccfc-89e8-4662-b940-ca7d267dcb99', 'SWA723-A375-00412', 'Kanlab Blue', 5,
        'Proin eget nisl non sapien gravida pellentesque. Cras tincidunt tortor posuere, laoreet sapien nec, tincidunt nunc. Integer vehicula, erat ut pretium porta, velit leo dignissim odio, eu ultricies urna nulla a dui. Proin et dapibus turpis, et tincidunt augue. In mattis luctus elit, in vehicula erat pretium sed. Suspendisse ullamcorper mollis dolor eu tristique.',
        'Proin eget nisl non sapien gravida pellentesque. Cras tincidunt tortor posuere, laoreet sapien nec, tincidunt nunc. Integer vehicula, erat ut pretium porta, velit leo dignissim odio, eu ultricies urna nulla a dui. Proin et dapibus turpis, et tincidunt augue. In mattis luctus elit, in vehicula erat pretium sed. Suspendisse ullamcorper mollis dolor eu tristique.',
        'generic-product-9.jpg',
        9.95, 0, 7, 1);
INSERT INTO messages (id, user_id, text, read)
values ('0fddab02-5ea0-4fb0-ae7d-c0b83679b9d4', 'e18c8bcc-935d-444d-a194-3a32a3b35a49', 'This is an example message',
        0);
INSERT INTO messages (id, user_id, text, read)
values ('2b87dbbe-4337-4f2a-b378-00277a49b82d', 'e18c8bcc-935d-444d-a194-3a32a3b35a49', 'Test message - please ignore!',
        0);
INSERT INTO messages (id, user_id, text, read)
values ('6914e47d-2f0a-4deb-a712-12e7801e13e8', '32e7db01-86bc-4687-9ecb-d79b265ac14f',
        'Welcome to JWA. This is an example message that you can read', 0);
INSERT INTO messages (id, user_id, text, read)
values ('755c10aa-fe8c-490b-82fa-8f418e39f596', '32e7db01-86bc-4687-9ecb-d79b265ac14f', 'Test message - please ignore!',
        0);
INSERT INTO messages (id, user_id, text, read)
values ('ec52da53-7809-4333-bfe5-84233f93be82', '92a82f45-7a03-42f3-80f8-ce4e9892409d',
        'Welcome to JWA. This is an example message that you can read', 0);
INSERT INTO messages (id, user_id, text, read)
values ('6aa03b01-4288-4e6e-bff6-dfe0f462cf68', '92a82f45-7a03-42f3-80f8-ce4e9892409d', 'Test message - please ignore!',
        0);

INSERT INTO orders (id, user_id, order_date, order_num, amount, shipped, cart)
VALUES ('c9b31f33-17a4-4fcd-927e-c14cdee32201', '32e7db01-86bc-4687-9ecb-d79b265ac14f', CURDATE()-5, 'OID-P400-0001', 100.0, 0,
        '[{"id":"6bbbeb10-6709-4163-a790-f691b09d6aca","name":"Dontax","price":8.50,"quantity":4}]');
INSERT INTO orders (id, user_id, order_date, order_num, amount, shipped, cart)
VALUES ('c94cbf6d-9baa-4a02-8eea-b13ceb43474d', '32e7db01-86bc-4687-9ecb-d79b265ac14f', CURDATE()-10, 'OID-P400-0001', 25.0, 0,
        '[{"id":"6bbbeb10-6709-4163-a790-f691b09d6aca","name":"Dontax","price":8.50,"quantity":1}]');
INSERT INTO orders (id, user_id, order_date, order_num, amount, shipped, shipped_date, cart)
VALUES ('81550b4f-c660-41ec-a6f3-076b611add9b', '32e7db01-86bc-4687-9ecb-d79b265ac14f', CURDATE()-20, 'OID-P401-0009', 50.0, 1, CURDATE(),
        '[{"id":"6bbbeb10-6709-4163-a790-f691b09d6aca","name":"Dontax","price":8.50,"quantity":2}]');
INSERT INTO orders (id, user_id, order_date, order_num, amount, shipped, cart)
VALUES ('db4cfab1-ff1d-4bca-a662-394771841383', 'db4cfab1-ff1d-4bca-a662-394771841383', CURDATE()-10, 'OID-G320-0051', 25.0, 0,
        '[{"id":"6bbbeb10-6709-4163-a790-f691b09d6aca","name":"Dontax","price":8.50,"quantity":1}]');

INSERT INTO reviews (id, product_id, user_id, review_date, comment, rating, visible)
VALUES ('822f734a-3d13-4ebc-bff6-9c36d29866a6', 'eec467c8-5de9-4c7c-8541-7b31614d31a0', '32e7db01-86bc-4687-9ecb-d79b265ac14f',
        CURDATE()-10, 'This is an example review of Solodox 750. It is very good.', 5, 1);
INSERT INTO reviews (id, product_id, user_id, review_date, comment, rating, visible)
VALUES ('5f3936db-0a41-4026-8a66-1b9b0c21e203', '74b87e87-0d77-422c-baaa-622498a84328', '32e7db01-86bc-4687-9ecb-d79b265ac14f',
        CURDATE()-5, 'Arrived on time and works well but the instructions are very limited and not explained well.', 4, 1);
INSERT INTO reviews (id, product_id, user_id, review_date, comment, rating, visible)
VALUES ('920292c5-0c9c-46a5-aacb-d8011ae6608a', 'eec467c8-5de9-4c7c-8541-7b31614d31a0', 'db4cfab1-ff1d-4bca-a662-394771841383',
        CURDATE()-2, 'This is another review of Solodox 750. It does not work as described and not worth the money.', 3, 1);
