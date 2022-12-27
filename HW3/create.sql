-- HW#3
-- Section 2
-- Student ID: 012568517
-- Hammar, Jason
-- In order to run this sql script wuth the ^ delimiter, use the follow command options
-- db2 -td"^" -f CREATE.sql
connect to CS157A^
CREATE TABLE hw3.student(
student_id char(6) NOT NULL,
First varchar(15) NOT NULL,
Last varchar(15) NOT NULL,
Gender char(1) NOT NULL,
PRIMARY KEY (student_id),
CHECK (Gender = 'M' OR Gender = 'F' OR Gender = 'O')
)^
CREATE TABLE hw3.class(
Class_id char(6) NOT NULL,
Name varchar(20) NOT NULL,
Desc varchar(20) NOT NULL,
PRIMARY KEY (Class_id)
)^
CREATE TABLE hw3.class_prereq(
Class_id char(6),
Prereq_id char(6) NOT NULL,
Req_Grade char(1) NOT NULL,
FOREIGN KEY (Class_id) REFERENCES hw3.class(Class_id) ON DELETE CASCADE,
FOREIGN KEY (Prereq_id) REFERENCES hw3.class(Class_id) ON DELETE CASCADE,
CHECK (Prereq_id != Class_id)
)^
CREATE TABLE hw3.schedule(
Student_id char(6),
Class_id char(6),
Semester int,
Year int,
Grade char(1) NULL,
FOREIGN KEY (Student_id) REFERENCES hw3.student(student_id) ON DELETE CASCADE,
FOREIGN KEY (Class_id) REFERENCES hw3.class(Class_id) ON DELETE CASCADE,
CHECK (Semester = 1 OR Semester = 2 OR Semester = 3),
CHECK (Year >= 1950 AND Year <= 2022),
CHECK (Grade = 'A' OR Grade = 'B' OR Grade = 'C' OR Grade = 'D' OR Grade = 'F' OR Grade = 'I' OR Grade = 'W')
)^
-- Try using a join statement to get these to work. Maybe look at HW2 Solution.

CREATE TRIGGER hw3.classcheck
NO CASCADE BEFORE INSERT ON hw3.schedule
REFERENCING NEW AS newrow  
FOR EACH ROW MODE DB2SQL
WHEN ( 0 < (SELECT COUNT(*)
              FROM hw3.class_prereq
              WHERE hw3.class_prereq.class_id = newrow.class_id) ) 
BEGIN ATOMIC
       DECLARE num_prereq int;
       DECLARE prereq_pass int;

       SET num_prereq = (SELECT COUNT(*)
                            FROM hw3.class_prereq 
                            WHERE hw3.class_prereq.class_id = newrow.class_id);
	SET prereq_pass = (SELECT COUNT(*)
				FROM hw3.class_prereq
				JOIN hw3.schedule ON newrow.student_id = hw3.schedule.student_id AND hw3.class_prereq.class_id = newrow.class_id AND hw3.class_prereq.prereq_id = hw3.schedule.class_id AND hw3.schedule.grade <= hw3.class_prereq.req_grade AND (hw3.schedule.year < newrow.year OR (hw3.schedule.year = newrow.year AND hw3.schedule.semester < newrow.semester)));
	

       IF ( num_prereq > 0) 
       THEN     IF (prereq_pass < num_prereq)
		THEN	SIGNAL SQLSTATE '88888' ( 'Missing pre-req' );
		END IF;
       END IF;
	
	
END^
	
terminate^
--FOREIGN KEY (PersonID) REFERENCES Persons(PersonID)
--SIGNAL SQLSTATE '88888' ( num_prereq );
