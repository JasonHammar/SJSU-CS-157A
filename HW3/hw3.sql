-- HW#3
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason
-- In order to run this sql script, use the follow command options
-- db2 -tvf HW3.sql
connect to cs157a;
delete from hw3.student;
delete from hw3.class;
delete from hw3.class_prereq;
delete from hw3.schedule;

--Inserting data into the student table
INSERT INTO hw3.student VALUES
       ('900000','John','Doe','M'),
       ('900001','Jane','Doe','F'),
       ('900002','James','Bond','M'),
       ('900003','Chris','Newman','O'),
       ('900004','Ken','Tsang','M');

--Inserting data into the class table
INSERT INTO hw3.class VALUES
       ('010000','CS100W','Technical Writing'),
       ('100000','CS46A','Intro to Programming'),
       ('100001','CS46B','Intro to Data Struct'),
       ('100002','CS47', 'Intro to Comp Sys'),
       ('100003','CS49J','Programming in Java'),
       ('200000','CS146','Data Structure & Alg'),
       ('200001','CS157A','Intro to DBMS'),
       ('200002','CS149','Operating Systems'),
       ('200003','CS160','Software Engineering'),
       ('200004','CS157B','DBMS II'),
       ('200005','CS157C','NoSQL DB Systems'),
       ('200006','CS151','OO Design'),
       ('200007','CS155','Design & Anal of Alg'),
       ('300000','CS257','DB Mgmt Principles'),
       ('300001','CS255','Design & Anal of Alg');

--Inserting data into the classreq table
INSERT INTO hw3.class_prereq VALUES
       ('100001','100000','C'),
       ('100002','100001','C'),
       ('200000','100001','C'),
       ('200001','200000','C'),
       ('200002','200000','C'),
       ('200003','010000','C'),
       ('200003','200000','C'),
       ('200003','200006','C'),
       ('200004','200001','C'),
       ('200005','200001','C'),
       ('200006','100001','C'),
       ('200007','200000','B'),
       ('300000','200004','B'),
       ('300001','200007','B');
 
  
--Test #1 (HW3.STUDENT)
INSERT INTO hw3.student VALUES ('000001','Test','Student 1','M');
INSERT INTO hw3.student VALUES ('000002','Test','Student 2','F');
INSERT INTO hw3.student VALUES ('000003','Test','Student 3','O');
-- error tests
INSERT INTO hw3.student VALUES ('000001','Duplicate','ID test','F');
INSERT INTO hw3.student VALUES (NULL, 'Test','Student 2','M');
INSERT INTO hw3.student VALUES ('000004', NULL,'Student 3','M');
INSERT INTO hw3.student VALUES ('000004', 'Test','Student 4','f');
INSERT INTO hw3.student VALUES ('000005', 'Test','Student 5','Z');
INSERT INTO hw3.student VALUES ('000006', 'Test','Student 6',NULL);
INSERT INTO hw3.student VALUES ('000006', 'Test',NULL,'M');

--Test #2 (HW3.CLASS)
INSERT INTO hw3.class VALUES ('000010','CS42','Discrete Mathematics');
INSERT INTO hw3.class VALUES ('000020','CS185C','Special Topics');
-- error tests
INSERT INTO hw3.class VALUES ('000010','Duplicate','ID test');
INSERT INTO hw3.class VALUES (NULL,'CS42','BadID Test');
INSERT INTO hw3.class VALUES ('000011',NULL,'NULL Test');
INSERT INTO hw3.class VALUES ('000012','CS25',NULL);

--Test #3 (HW3.CLASS_PREREQ) Error tests
INSERT INTO hw3.class_prereq VALUES ('000010','999999','D');
INSERT INTO hw3.class_prereq VALUES ('000010',NULL,'C');
INSERT INTO hw3.class_prereq VALUES ('999999','000010','F');
INSERT INTO hw3.class_prereq VALUES ('100000','000010',NULL);

--Test #4 (HW3.SCHEDULE) non-existing class id and student id (Foregin Key constraint)
INSERT INTO hw3.schedule VALUES ('999999','100000',1,2019,'A');
INSERT INTO hw3.schedule VALUES ('000001','999999',2,2019,'B');
--Test invalid values which should be block by CHECK contraints, invalid semester & grades
INSERT INTO hw3.schedule VALUES ('000001','000020',4,2019,'C');
INSERT INTO hw3.schedule VALUES ('000002','000020',3,2019,'E');
INSERT INTO hw3.schedule VALUES ('000003','000020',3,2019,'a');

--Test cascade drop of HW3.CLASS (Question 2 from spec), the 2 (SELECT * ...) stmts after DELETE should have 0 record
INSERT INTO hw3.schedule VALUES ('000001','000020',1,2019,'A');
INSERT INTO hw3.class_prereq VALUES ('000020','000010','C');
SELECT * FROM hw3.class_prereq WHERE hw3.class_prereq.class_id = '000020';
SELECT * FROM hw3.schedule WHERE hw3.schedule.class_id = '000020';
DELETE FROM hw3.class WHERE hw3.class.class_id = '000020';
SELECT * FROM hw3.class_prereq WHERE hw3.class_prereq.class_id = '000020';
SELECT * FROM hw3.schedule WHERE hw3.schedule.class_id = '000020';

--Test #5 Test Trigger, first empty the table just in case
DELETE from hw3.schedule;
-- Start with student '900000'
-- Add CS100W, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900000','010000',1,2018,'C');
-- Add CS46B but no CS46A yet, should fail
INSERT INTO hw3.schedule VALUES ('900000','100001',2,2018,NULL); 
-- Add CS46A without grade, should work!
INSERT INTO hw3.schedule VALUES ('900000','100000',1,2018,NULL); 
-- Add CS46B but no CS46A grade, should fail
INSERT INTO hw3.schedule VALUES ('900000','100001',1,2018,NULL); 
-- Update CS46A grade to 'F'
UPDATE hw3.schedule SET hw3.schedule.grade = 'F' where hw3.schedule.student_id = '900000' AND hw3.schedule.class_id = '100000'; 
-- Add CS46B but CS47A grade is 'F', should fail 
INSERT INTO hw3.schedule VALUES ('900000','100001',1,2018,NULL); 
-- Update CS46A grade to 'B'
UPDATE hw3.schedule SET hw3.schedule.grade = 'B' where hw3.schedule.student_id = '900000' AND hw3.schedule.class_id = '100000';  
-- Add CS46B with CS46A grade = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900000','100001',2,2018,'A'); 
-- Add CS151 with prereq CS46B = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900000','200006',3,2018,'B'); 
-- Add CS160, has CS151,CS100W, but no CS146, should FAIL
INSERT INTO hw3.schedule VALUES ('900000','200003',1,2019,'B'); 
-- Add CS157A but missing prereq, should fail
INSERT INTO hw3.schedule VALUES ('900000','200001',1,2019,'B'); 
-- Add CS146 with prereq CS46B = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900000','200000',3,2018,'A'); 
-- Add CS157A with prereq CS146 = 'A', should work!
INSERT INTO hw3.schedule VALUES ('900000','200001',1,2019,'B'); 
-- Add CS160, has CS151,CS100W,and CS146, should work!!!
INSERT INTO hw3.schedule VALUES ('900000','200003',1,2019,'B'); 
-- Finished with student '900000'
-- Test with student '900001'
-- Add CS100W, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900001','010000',1,2018,'A'); 
-- Add CS46B but no CS46A yet, should fail
INSERT INTO hw3.schedule VALUES ('900001','100001',2,2018,'B'); 
-- Add CS157A but missing prereq, should fail
INSERT INTO hw3.schedule VALUES ('900001','200001',1,2019,'B'); 
-- Add CS46A, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900001','100000',1,2018,'A'); 
-- Add CS46B with CS46A grade = 'A', should work!
INSERT INTO hw3.schedule VALUES ('900001','100001',2,2018,'A'); 
-- Add CS160, has CS100W, but no CS146,CS151, should FAIL
INSERT INTO hw3.schedule VALUES ('900001','200003',1,2019,'B'); 
-- Add CS151 with prereq CS46B = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900001','200006',3,2018,'B'); 
-- Add CS146 with prereq CS46B = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900001','200000',3,2018,'F'); 
-- Add CS160, has CS151,CS100W,but CS146='F', should FAIL
INSERT INTO hw3.schedule VALUES ('900001','200003',1,2019,'B'); 
-- Update CS146 grade to 'B'
UPDATE hw3.schedule SET hw3.schedule.grade = 'B' where hw3.schedule.student_id = '900001' AND hw3.schedule.class_id = '200000';  
-- Add CS160, has CS151,CS100W,and CS146, should work!!!
INSERT INTO hw3.schedule VALUES ('900001','200003',1,2019,'B'); 
-- Finished with student '9000'
-- Test with student '9002'
-- Add CS46A, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900002','100000',1,2020,'D'); 
-- Add CS46A again in a later semester, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900002','100000',1,2021,'C'); 
-- Add CS46A again in a later semester, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900002','100000',2,2021,'A'); 
-- Add CS46B with more than 1 CS46A grades (highest is 'A') should work!
INSERT INTO hw3.schedule VALUES ('900002','100001',3,2021,'B');
-- Add CS146 with prereq CS46B = 'B', should work!
-- Finished with student '9002'
-- Test with student '9003'
-- Add CS46A, no prereq, should work!
INSERT INTO hw3.schedule VALUES ('900003','100000',1,2020,'B'); 
-- Add CS46B with CS46A grade = 'A', should work!
INSERT INTO hw3.schedule VALUES ('900003','100001',2,2020,'A'); 
-- Add CS146 with prereq CS46B = 'B', should work!
INSERT INTO hw3.schedule VALUES ('900003','200000',3,2020,'C'); 
-- Add CS155 with prereq CS146B = 'C', req a 'B' should fail!
INSERT INTO hw3.schedule VALUES ('900003','200007',1,2021,'C'); 
-- Update CS146 grade to 'B'
UPDATE hw3.schedule SET hw3.schedule.grade = 'B' where hw3.schedule.student_id = '900003' AND hw3.schedule.class_id = '200000';  
-- Add CS155 with prereq CS146B = 'B', req a 'B' should work!
INSERT INTO hw3.schedule VALUES ('900003','200007',1,2021,'C'); 
-- Add CS255 with prereq CS155 = 'C', req a 'B' should fail!
INSERT INTO hw3.schedule VALUES ('900003','300001',2,2021,'A'); 
-- Update CS155 grade to 'A'
UPDATE hw3.schedule SET hw3.schedule.grade = 'A' where hw3.schedule.student_id = '900003' AND hw3.schedule.class_id = '200007';  
-- Add CS255 with prereq CS155 = 'A', req a 'B' should work!
INSERT INTO hw3.schedule VALUES ('900003','300001',2,2021,'A'); 
-- Finished with student '9003'
-- The End

-- Check table contents after all the tests
select * from hw3.student order by student_id;
select * from hw3.class order by class_id;
select * from hw3.class_prereq order by class_id;
select * from hw3.schedule order by student_id, class_id;

-- Deletes the data from the tables for more tests.
delete from hw3.schedule;
-- These are all of my tests that will test different parts of the trigger.

-- This line will put CS 46A into the schedule, it has no prereqs, so it will be entered.
INSERT INTO hw3.schedule VALUES ('900000', '100000', 1, 2012, 'A');
-- This next line will test whether or not a course with a prereq will be entered even though the student is not in the schedule.
-- This statement will fail, and the error message missing prereq will be printed.
INSERT INTO hw3.schedule VALUES ('900000', '100001', 1, 2012, 'A');
-- This next statement will work because it's prereq has been satisfied.
INSERT INTO hw3.schedule VALUES ('900000', '100001', 3, 2012, 'A');
-- This student doesn't have a class schedule, because of that, he is missing prereqs for this class, so this line will fail.
INSERT INTO hw3.schedule VALUES ('900002', '100001', 3, 2012, 'A');
-- Another Student is entering in his information for CS 46A, This statement will pass.
INSERT INTO hw3.schedule VALUES ('900001', '100000', 3, 2012, 'A');
-- Inserts CS100W, which has no prereqs, this should pass.
INSERT INTO hw3.schedule VALUES ('900000', '010000', 1, 2012, 'A');
-- These next two statements have the same prereq, and this student already passed it. This statment will pass.
INSERT INTO hw3.schedule VALUES ('900000', '100002', 1, 2013, 'B');
INSERT INTO hw3.schedule VALUES ('900000', '200000', 1, 2013, 'B');
-- These next two statements have the same prereq, However, they are trying to take these classes at the same term as the prereq. These statments will fail.
INSERT INTO hw3.schedule VALUES ('900000', '200001', 1, 2013, 'D');
INSERT INTO hw3.schedule VALUES ('900000', '200002', 1, 2013, 'D');
-- This statment will work, because although the year is the same as the prereq, the semester is later, so it's a later term.
INSERT INTO hw3.schedule VALUES ('900000', '200001', 3, 2013, 'D');
-- This statement will not work because it's prereq grade is not passing.
INSERT INTO hw3.schedule VALUES ('900000', '200004', 1, 2014, 'C');
-- A student can retake a class, so the student will now have a better grade in the prereq course and the statement below that will get added.
INSERT INTO hw3.schedule VALUES ('900000', '200001', 1, 2014, 'C');
INSERT INTO hw3.schedule VALUES ('900000', '200004', 2, 2014, 'C');
-- This next statement will fail because the student is trying to take a prereq a year before they actually can.
INSERT INTO hw3.schedule VALUES ('900000', '200005', 1, 2012, 'C');
-- This statement will not work because the term listed is the same term the prereq is taken.
INSERT INTO hw3.schedule VALUES ('900000', '200004', 1, 2014, 'B');
-- This statment will fail because although two prereqs have been met, there are three prereqs that need to be satisfied, so that one needs to be done first.
INSERT INTO hw3.schedule VALUES ('900000', '200003', 3, 2014, 'A');
-- With all prereqs met, these statments should work.
INSERT INTO hw3.schedule VALUES ('900000', '200006', 1, 2014, 'C');
INSERT INTO hw3.schedule VALUES ('900000', '200003', 3, 2014, 'A');
-- Because grades are nullable, this will be able to run
INSERT INTO hw3.schedule VALUES ('900000', '200007', 1, 2015, NULL);
-- This statement will not work because there has to be a grade, a student can't register for a class that they haven't completed.
INSERT INTO hw3.schedule VALUES ('900000', '300001', 2, 2015, 'A');
-- Now that the class has been completed, both of these values should pass.
INSERT INTO hw3.schedule VALUES ('900000', '200007', 1, 2015, 'B');
INSERT INTO hw3.schedule VALUES ('900000', '300001', 2, 2015, 'A');
-- Shows the hw3.schedule table
select * from hw3.schedule;

-- The statments below are adding in values for one of the other students. All of them should pass. This is to show that the schedule for one student won't affect another student.

-- All of these statements should work
INSERT INTO hw3.schedule VALUES ('900002', '100000', 1, 2012, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '100001', 1, 2013, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '100002', 2, 2013, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '200000', 3, 2013, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '010000', 3, 2013, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '200006', 2, 2014, 'A');
INSERT INTO hw3.schedule VALUES ('900002', '200003', 3, 2014, 'A');

select * from hw3.schedule;
terminate;