-- Database Migration Script for Adding Role Column
-- Run this script in your PostgreSQL database before starting the application

-- Step 1: Add the role column as nullable first
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(255);

-- Step 2: Set default role for existing users
-- The first user will be ADMIN, rest will be USER
UPDATE users 
SET role = CASE 
    WHEN id = (SELECT MIN(id) FROM users) THEN 'ADMIN'
    ELSE 'USER'
END
WHERE role IS NULL;

-- Step 3: Make the column NOT NULL
ALTER TABLE users ALTER COLUMN role SET NOT NULL;

-- Verify the changes
SELECT id, username, email, role, status FROM users ORDER BY id;
