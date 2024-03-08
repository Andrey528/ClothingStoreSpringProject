use clothingshop;

insert into products (img_src, title, description, price, color, size, reserved_quantity, store_amount)
values
('http://localhost:8081/product/1/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 52.0, 'red', 'XL', 0, 15),
('http://localhost:8081/product/2/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 22.0, 'red', 'XL', 0, 15),
('http://localhost:8081/product/3/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 32.0, 'red', 'XL', 0, 15),
('http://localhost:8081/product/4/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 53.0, 'red', 'XL', 0, 15),
('http://localhost:8081/product/5/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 55.0, 'red', 'XL', 0, 15),
('http://localhost:8081/product/6/productimg', "ELLERY X M'O CAPSULE", 'Known for her sculptural takes on traditional tailoring, Australian arbiter of cool Kym Ellery teams up with Moda Operandi.', 47.0, 'red', 'XL', 0, 15);

use walletdb;

INSERT INTO bank_account (id, card_number, balance) VALUES (1, 1234567890, 1000.00);
INSERT INTO users (id, username, bank_account_id) VALUES (1, 'example_user', 1);