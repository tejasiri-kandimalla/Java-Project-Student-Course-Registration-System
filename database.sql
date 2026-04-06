CREATE DATABASE IF NOT EXISTS student_course_db;
USE student_course_db;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'admin' or 'student'
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    department VARCHAR(50),
    year VARCHAR(20)
);

-- Sample Data (Admin & Student)
-- Admin: username='admin', password='12345'
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', '$2a$12$jCag/xBGrPjUxTsf/BbStOgEZd.jB6Ta7IXOBcZKbTHMr9wrvyde6', 'admin');

-- Student: Student ID as username (e.g., 'S1001'), 12345
INSERT IGNORE INTO users (username, password, role) VALUES ('S1001', '$2a$12$jCag/xBGrPjUxTsf/BbStOgEZd.jB6Ta7IXOBcZKbTHMr9wrvyde6', 'student');

-- Insert sample student profile matching the 'S1001' user
INSERT IGNORE INTO students (student_id, name, email, department, year) 
VALUES ('S1001', 'Demo Student', 'student@example.com', 'CSE', '3rd Year');

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    credits INT,
    semester INT
);

-- Sample Courses
INSERT IGNORE INTO courses (course_id, course_name, department, credits, semester) VALUES 
('C101', 'Data Structures', 'CSE', 4, 3),
('C102', 'Database Management Systems', 'CSE', 4, 4),
('E201', 'Digital Circuits', 'ECE', 3, 3);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50),
    course_id VARCHAR(20),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    UNIQUE(student_id, course_id)
);
