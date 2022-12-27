-- HW#3
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason
-- In order to run this sql script, use the follow command options
-- db2 -tvf DROP.sql
connect to CS157A;
DROP TABLE hw3.student;
DROP TABLE hw3.class;
DROP TABLE hw3.class_prereq;
DROP TABLE hw3.schedule;
DROP TRIGGER hw3.classcheck;
terminate;