DROP TABLE IF EXISTS passages_has_answers;
DROP TABLE IF EXISTS user_has_course;
DROP TABLE IF EXISTS user_roles_has_users;
DROP TABLE IF EXISTS materials_has_content;
DROP TABLE IF EXISTS lesson_has_tests;
DROP TABLE IF EXISTS courses_has_teachers;
DROP TABLE IF EXISTS materials;
DROP TABLE IF EXISTS passages;
DROP TABLE IF EXISTS lesson;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS course_period;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS tests;
DROP TABLE IF EXISTS content;

DROP TYPE IF EXISTS answer_type_enum CASCADE;
DROP TYPE IF EXISTS content_type_enum;
DROP TYPE IF EXISTS role_type_enum;

-- -----------------------------------------------------
-- Table users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  firstName TEXT NOT NULL,
  secondName TEXT NOT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt)
);
-- -----------------------------------------------------
-- Table passages
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS passages (
  id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  startDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  endDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  testid INT NOT NULL,
  result INT NOT NULL,
  is_right BOOLEAN NOT NULL,
  PRIMARY KEY (startDate, id)
);

CREATE TABLE IF NOT EXISTS answers (
  id SERIAL PRIMARY KEY,
  userAnswer TEXT NOT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt)
);

CREATE TABLE IF NOT EXISTS passages_has_answers (
  id INT NOT NULl,
  passagesStartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  answerId INT NOT NULL REFERENCES answers(id) ON DELETE CASCADE,
  FOREIGN KEY (id, passagesStartDate) REFERENCES passages(id,startDate) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table courses
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS courses (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  subject TEXT NOT NULL,
  imageUrl TEXT NOT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt)
);


-- -----------------------------------------------------
-- Table lesson
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lesson (
  id SERIAL PRIMARY KEY,
  coursesId INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  title TEXT NOT NULL
 );


-- -----------------------------------------------------
-- Table materials
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS materials (
  id SERIAL PRIMARY KEY,
  title TEXT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt),
  lessonId INT NOT NULL REFERENCES lesson(id) ON DELETE CASCADE
);


-- -----------------------------------------------------
-- Table tests
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tests (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  threshold INT NOT NULL,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt)
);


-- -----------------------------------------------------
-- Table questions
-- -----------------------------------------------------
CREATE TYPE answer_type_enum AS ENUM('Single', 'Multy');

CREATE TABLE IF NOT EXISTS questions (
  id SERIAL PRIMARY KEY,
  textQuestion TEXT NOT NULL,
  answerType answer_type_enum NOT NULL,
  testid INT NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
  rightAnswerId INT NOT NULL REFERENCES answers(id) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table user_has_course
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_has_course (
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP CONSTRAINT check_update_date CHECK(updatedAt >= createdAt),
  courseid INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);


-- -----------------------------------------------------
-- Table user_roles_has_users
-- -----------------------------------------------------


CREATE TYPE role_type_enum AS ENUM('Student', 'Teacher', 'Admin');

CREATE TABLE IF NOT EXISTS user_roles_has_users (
  id SERIAL PRIMARY KEY,
  userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role role_type_enum NOT NULL,
  startDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  endDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 );


-- -----------------------------------------------------
-- Table courses_has_teachers
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS courses_has_teachers (
  coursesId INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);


-- -----------------------------------------------------
-- Table content
-- -----------------------------------------------------

CREATE TYPE content_type_enum AS ENUM('Image', 'Video', 'Audio', 'Text');

CREATE TABLE IF NOT EXISTS content (
  id SERIAL PRIMARY KEY,
  content TEXT NOT NULL,
  contentType content_type_enum NULL
);


-- -----------------------------------------------------
-- Table materials_has_content
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS materials_has_content (
  materialId INT NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
  contentId INT NOT NULL REFERENCES content(id) ON DELETE CASCADE
);


-- -----------------------------------------------------
-- Table coursePeriod
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS course_period (
  coursesId INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  startDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  endDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- -----------------------------------------------------
-- Table lesson_has_tests
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS lesson_has_tests (
  lessonId INT NOT NULL REFERENCES lesson(id) ON DELETE CASCADE,
  testsId INT NOT NULL REFERENCES tests(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION check_id() RETURNS trigger AS $check_id$
    DECLARE
    foundId INTEGER;
BEGIN
  EXECUTE 'SELECT id from ' || TG_TABLE_NAME || ' where id = ' || NEW.id || ''  INTO foundId;
	IF (foundId <> NULL) THEN
		RAISE EXCEPTION 'Пользователь с таким ID уже существует';
	END IF;
RETURN NEW;
END
$check_id$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_role() RETURNS trigger AS $check_role$
DECLARE A RECORD;
BEGIN
	SELECT id, role INTO A FROM user_roles_has_users where id = NEW.id;
        IF (A.id = NEW.id AND A.role = NEW.role) THEN
            RAISE EXCEPTION 'У пользователя уже есть такая роль';
            END IF;
END
$check_role$ LANGUAGE plpgsql;

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON user_roles_has_users
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON materials
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON lesson
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON questions
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON users
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON answers
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON courses
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON tests
    FOR EACH ROW EXECUTE PROCEDURE check_id();

CREATE TRIGGER check_id BEFORE INSERT OR UPDATE ON content
    FOR EACH ROW EXECUTE PROCEDURE check_id();
