-- schema.sql

-- Drop tables in reverse order of dependency due to foreign keys
-- order_items depends on orders and products
-- cart_items depends on carts and products
-- carts depends on users
-- orders depends on users
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Create the users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing primary key for MySQL
    name VARCHAR(255) NOT NULL,           -- User's name, cannot be null
    email VARCHAR(255) NOT NULL UNIQUE,   -- User's email, must be unique and cannot be null
    password VARCHAR(255) NOT NULL        -- User's password (will store plain text as per current AuthService, INSECURE for production)
);

-- Create the products table
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing primary key for MySQL
    name VARCHAR(255) NOT NULL,           -- Product name, cannot be null
    description VARCHAR(1000),            -- Product description, up to 1000 characters
    price DECIMAL(10, 2) NOT NULL,        -- Product price, with 10 total digits and 2 decimal places, cannot be null
    image_url VARCHAR(255)                -- URL for product image
);

-- Create the carts table
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- One-to-one relationship with users, so user_id must be unique
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the cart_items table
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,    -- Foreign key to the carts table
    product_id BIGINT NOT NULL,  -- Foreign key to the products table
    quantity INT NOT NULL,
    -- Foreign key constraints
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    -- Ensure a product is unique within a specific cart
    UNIQUE (cart_id, product_id)
);

-- Create the orders table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- E.g., PENDING, SHIPPED, DELIVERED
    shipping_address VARCHAR(255),
    shipping_city VARCHAR(100),
    shipping_postal_code VARCHAR(20),
    shipping_country VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the order_items table
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Link to product for current details
    quantity INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    price_at_order DECIMAL(10, 2) NOT NULL,
    product_image_url VARCHAR(255),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT -- RESTRICT to prevent deleting product if it's in an order
);
