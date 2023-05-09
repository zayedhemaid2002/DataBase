CREATE TABLE course (
  id int NOT NULL,
  name varchar(255)  DEFAULT NULL,
  room varchar(255)  DEFAULT NULL
);


INSERT INTO course  VALUES(1, 'programing', '104');
INSERT INTO course  VALUES(2, 'programing3', '102');


ALTER TABLE course ADD PRIMARY KEY (id);






CREATE TABLE registration (
  studentId int NOT NULL,
  courseId int NOT NULL,
  semester varchar(255)  DEFAULT NULL
) ;

INSERT INTO registration  VALUES(1, 1, 'Summer');
INSERT INTO registration  VALUES(2, 1, 'wenter');


ALTER TABLE registration ADD PRIMARY KEY (studentId,courseId);





CREATE TABLE student (
  id int NOT NULL,
  name varchar(255)  NOT NULL,
  major varchar(255) NOT NULL,
  grade float NOT NULL
);


INSERT INTO student  VALUES(1, 'ali', 'it', 80);
INSERT INTO student  VALUES(2, 'ahmed', 'it', 75);


ALTER TABLE studen ADD PRIMARY KEY (id);


ALTER TABLE registration
  ADD (CONSTRAINT registration_ibfk_1 FOREIGN KEY (studentId) REFERENCES student (id))
  ADD (CONSTRAINT registration_ibfk_2 FOREIGN KEY (courseId) REFERENCES course (id));
COMMIT;