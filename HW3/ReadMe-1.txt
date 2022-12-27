-- HW#3
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason

In order to run this program you must first run CREATE.sql. 

USE THE COMMAND: db2 -td"^" -f create.sql TO RUN THIS SCRIPT, due to the delimiter being "^". Using the tvf command will cause an error. 

This will create all of the tables as well as the trigger that will help the program decipher if there has been an invalid insertion.

Aftwards, run hw3.sql. This file will insert all of the values into the class, student, and class_prereq.
Then, it will test all of the constraints placed on each of the values in each table, and show statements that work and don't work.
This script will then test the trigger and make sure that no invalid entries are permitted. Use the command: db2 -tvf HW3.sql. It will test all of the conditions that have been lined out in CREATE.sql.
hw3.sql has comments explaining the purpose of each line and what will happen when each line is called.

Then, run drop.sql in order to drop all of the tables and the trigger, in order to clean everything up. Use the command: db2 -tvf DROP.sql.