-- data.sql

-- Insert sample data into the products table
-- The 'id' column is auto-incremented, so we don't include it in the INSERT statement.
DELETE FROM products; -- Clear existing data to avoid duplicates
INSERT INTO products (name, description, price, image_url) VALUES
('Laptop Pro X', 'Powerful laptop with 16GB RAM, 512GB SSD, and a high-resolution display. Perfect for professionals and gamers alike.', 1299.99, 'https://placehold.co/600x400/000000/FFFFFF?text=Laptop'),
('Wireless Ergonomic Mouse', 'Comfortable and precise wireless mouse designed for long hours of use. Features adjustable DPI and programmable buttons.', 29.99, 'https://placehold.co/600x400/FF0000/FFFFFF?text=Mouse'),
('Mechanical Keyboard RGB', 'Full-size mechanical keyboard with customizable RGB backlighting and satisfying tactile switches. Durable and responsive.', 89.95, 'https://placehold.co/600x400/00FF00/FFFFFF?text=Keyboard'),
('4K UHD Monitor 27"', 'Stunning 27-inch 4K UHD monitor with vibrant colors and wide viewing angles. Ideal for graphic design and entertainment.', 349.00, 'https://placehold.co/600x400/0000FF/FFFFFF?text=Monitor'),
('USB-C Hub 7-in-1', 'Compact USB-C hub with HDMI, USB 3.0, SD card reader, and power delivery. Expands your laptop''s connectivity.', 45.50, 'https://placehold.co/600x400/FFFF00/000000?text=USB+Hub');

-- Add more sample data as needed
INSERT INTO products (name, description, price, image_url) VALUES
('External SSD 1TB', 'Portable 1TB SSD for fast data transfer and reliable storage. USB 3.2 Gen 2 compatible.', 99.99, 'https://placehold.co/600x400/FF00FF/FFFFFF?text=SSD'),
('Noise-Cancelling Headphones', 'Premium over-ear headphones with active noise cancellation and crystal-clear audio. Long-lasting battery.', 199.00, 'https://placehold.co/600x400/00FFFF/000000?text=Headphones');



-- data.sql for User Model

-- Insert sample data into the users table
-- The 'id' column is auto-incremented, so we don't include it in the INSERT statement.
-- IMPORTANT: Passwords are stored in plain text here as per the simplified AuthService.
-- This is HIGHLY INSECURE and should NEVER be done in a production environment.
-- In a real application, passwords MUST be hashed (e.g., using BCrypt) before storage.
DELETE FROM users; -- Clear existing data to avoid duplicates
INSERT INTO users (name, email, password) VALUES
('John Doe', 'john.doe@example.com', 'password123'),
('Jane Smith', 'jane.smith@example.com', 'securepass'),
('Alice Brown', 'alice.brown@example.com', 'mysecret');

-- Add more sample user data as needed
INSERT INTO users (name, email, password) VALUES
('Bob White', 'bob.white@example.com', 'anotherpassword'),
('Charlie Green', 'charlie.green@example.com', 'greenpass');
