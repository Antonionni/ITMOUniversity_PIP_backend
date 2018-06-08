create table coursach.answers (id  serial not null, createdat timestamp, questionid int4 not null, updatedat timestamp, useranswer VARCHAR not null, primary key (id))
create table coursach.content (id  serial not null, content VARCHAR not null, contenttype int4 not null, primary key (id))
create table coursach.course_period (coursesid  serial not null, enddate timestamp, startdate timestamp, primary key (coursesid))
create table coursach.courses (id  serial not null, createdat timestamp, imageurl VARCHAR not null, subject VARCHAR not null, title VARCHAR not null, updatedat timestamp, primary key (id))
create table coursach.lesson (id  serial not null, coursesid int4 not null, title VARCHAR not null, primary key (id))
create table coursach.linkedAccount (id  bigserial not null, providerKey varchar(255), providerUserId varchar(255), user_id int4, primary key (id))
create table coursach.materials (id  serial not null, createdat timestamp, lessonid int4 not null, title varchar(255), updatedat timestamp, primary key (id))
create table coursach.passages (id  serial not null, startdate timestamp not null, enddate timestamp, result int4 not null, is_right boolean not null, testid int4 not null, primary key (id, startdate))
create table coursach.passages_has_answers (id  serial not null, answerid int4 not null, passagesstartdate timestamp, primary key (id))
create table coursach.questions (id  serial not null, answertype int4 not null, rightanswerid int4 not null, testid int4 not null, textquestion VARCHAR not null, primary key (id))
create table coursach.tests (id  serial not null, createdat timestamp, threshold int4 not null, title VARCHAR not null, updatedat timestamp, primary key (id))
create table coursach.tokenAction (id  bigserial not null, created timestamp, expires timestamp, token varchar(255), type int4 not null, targetUser_id int4, primary key (id))
create table coursach.user_has_course (id  serial not null, courseid int4 not null, createdat timestamp, updatedat timestamp, primary key (id))
create table coursach.user_roles_has_users (id  serial not null, enddate timestamp, role int4 not null, startdate timestamp, userId int4 not null, primary key (id))
create table coursach.users (id  serial not null, isActive boolean, createdat timestamp, email varchar(400) not null, emailValidated boolean, firstname varchar(255), lastLogin timestamp, name varchar(255), secondname varchar(255), updatedat timestamp, primary key (id))
alter table coursach.tokenAction add constraint UK_7px1dd6y3hg2uk86815v98frd unique (token)
create table coursach.courses_has_teachers (coursesid int4 not null, userid int4 not null)
create table coursach.lesson_has_tests (lessonid int4 not null, testsid int4 not null)
create table coursach.materials_has_content (materialid int4 not null, contentid int4 not null)
alter table coursach.answers add constraint FKd24h2220v89v6vfr9tworifas foreign key (questionid) references coursach.questions
alter table coursach.course_period add constraint FKl6stfuf96h07a1sl38xn60f7m foreign key (coursesid) references coursach.courses
alter table coursach.lesson add constraint FKo58cmxb9oq4o8ug0p9085uqo1 foreign key (coursesid) references coursach.courses
alter table coursach.linkedAccount add constraint FKthv03et66sg5nwa21b72mrgoj foreign key (user_id) references coursach.users
alter table coursach.materials add constraint FKa83ehwiwytnpa8w4q1qrbm6f8 foreign key (lessonid) references coursach.lesson
alter table coursach.passages add constraint FKgfaajx1ye9ndjwq2efmsmcii8 foreign key (id) references coursach.users
alter table coursach.passages_has_answers add constraint FK7gyc3ul8fhtxgcco5p7j4bpus foreign key (answerid) references coursach.answers
alter table coursach.passages_has_answers add constraint FKrxdsuipe4c6bc2rmtl4y7yvn0 foreign key (id, passagesstartdate) references coursach.passages
alter table coursach.questions add constraint FKh1de9ktj0m7uv2nydjhvbo4mt foreign key (rightanswerid) references coursach.answers
alter table coursach.questions add constraint FKf72hna94f2chwhwdxcykufk0k foreign key (testid) references coursach.tests
alter table coursach.tokenAction add constraint FKsuubsgx8qrrggtv9gg2ko963q foreign key (targetUser_id) references coursach.users
alter table coursach.user_has_course add constraint FK7ycpllljm7tfwb5iv9jaieew foreign key (courseid) references coursach.courses
alter table coursach.user_has_course add constraint FKlgsbp2k7f897kj6skeikw2ohc foreign key (id) references coursach.users
alter table coursach.user_roles_has_users add constraint FK940jtf09fainb5ymd07gqgiud foreign key (userId) references coursach.users
alter table coursach.courses_has_teachers add constraint FKan03ga59pwgeu924wp4ph1ax1 foreign key (userid) references coursach.users
alter table coursach.courses_has_teachers add constraint FKdx2ti8wu9pakdrf20wcj388if foreign key (coursesid) references coursach.courses
alter table coursach.lesson_has_tests add constraint FK3mjjy3givepogniivkk7sashs foreign key (testsid) references coursach.tests
alter table coursach.lesson_has_tests add constraint FKd5sdim5drdyhwh7phxkosac9g foreign key (lessonid) references coursach.lesson
alter table coursach.materials_has_content add constraint FK25d6utcobt44ku0yho80q5926 foreign key (contentid) references coursach.content
alter table coursach.materials_has_content add constraint FKqhr0ntk7suqislqju4bfn67cg foreign key (materialid) references coursach.materials
create table coursach.answers (id  serial not null, createdat timestamp, questionid int4 not null, updatedat timestamp, useranswer VARCHAR not null, primary key (id))
create table coursach.content (id  serial not null, content VARCHAR not null, contenttype int4 not null, primary key (id))
create table coursach.course_period (coursesid  serial not null, enddate timestamp, startdate timestamp, primary key (coursesid))
create table coursach.courses (id  serial not null, createdat timestamp, imageurl VARCHAR not null, subject VARCHAR not null, title VARCHAR not null, updatedat timestamp, primary key (id))
create table coursach.lesson (id  serial not null, coursesid int4 not null, title VARCHAR not null, primary key (id))
create table coursach.linkedAccount (id  bigserial not null, providerKey varchar(255), providerUserId varchar(255), user_id int4, primary key (id))
create table coursach.materials (id  serial not null, createdat timestamp, lessonid int4 not null, title varchar(255), updatedat timestamp, primary key (id))
create table coursach.passages (id  serial not null, startdate timestamp not null, enddate timestamp, result int4 not null, is_right boolean not null, testid int4 not null, primary key (id, startdate))
create table coursach.passages_has_answers (id  serial not null, answerid int4 not null, passagesstartdate timestamp, primary key (id))
create table coursach.questions (id  serial not null, answertype int4 not null, rightanswerid int4 not null, testid int4 not null, textquestion VARCHAR not null, primary key (id))
create table coursach.tests (id  serial not null, createdat timestamp, threshold int4 not null, title VARCHAR not null, updatedat timestamp, primary key (id))
create table coursach.tokenAction (id  bigserial not null, created timestamp, expires timestamp, token varchar(255), type int4 not null, targetUser_id int4, primary key (id))
create table coursach.user_has_course (id  serial not null, courseid int4 not null, createdat timestamp, updatedat timestamp, primary key (id))
create table coursach.user_roles_has_users (id  serial not null, enddate timestamp, role int4 not null, startdate timestamp, userId int4 not null, primary key (id))
create table coursach.users (id  serial not null, isActive boolean, createdat timestamp, email varchar(400) not null, emailValidated boolean, firstname varchar(255), lastLogin timestamp, name varchar(255), secondname varchar(255), updatedat timestamp, primary key (id))
alter table coursach.tokenAction add constraint UK_7px1dd6y3hg2uk86815v98frd unique (token)
create table coursach.courses_has_teachers (coursesid int4 not null, userid int4 not null)
create table coursach.lesson_has_tests (lessonid int4 not null, testsid int4 not null)
create table coursach.materials_has_content (materialid int4 not null, contentid int4 not null)
alter table coursach.answers add constraint FKd24h2220v89v6vfr9tworifas foreign key (questionid) references coursach.questions
alter table coursach.course_period add constraint FKl6stfuf96h07a1sl38xn60f7m foreign key (coursesid) references coursach.courses
alter table coursach.lesson add constraint FKo58cmxb9oq4o8ug0p9085uqo1 foreign key (coursesid) references coursach.courses
alter table coursach.linkedAccount add constraint FKthv03et66sg5nwa21b72mrgoj foreign key (user_id) references coursach.users
alter table coursach.materials add constraint FKa83ehwiwytnpa8w4q1qrbm6f8 foreign key (lessonid) references coursach.lesson
alter table coursach.passages add constraint FKgfaajx1ye9ndjwq2efmsmcii8 foreign key (id) references coursach.users
alter table coursach.passages_has_answers add constraint FK7gyc3ul8fhtxgcco5p7j4bpus foreign key (answerid) references coursach.answers
alter table coursach.passages_has_answers add constraint FKrxdsuipe4c6bc2rmtl4y7yvn0 foreign key (id, passagesstartdate) references coursach.passages
alter table coursach.questions add constraint FKh1de9ktj0m7uv2nydjhvbo4mt foreign key (rightanswerid) references coursach.answers
alter table coursach.questions add constraint FKf72hna94f2chwhwdxcykufk0k foreign key (testid) references coursach.tests
alter table coursach.tokenAction add constraint FKsuubsgx8qrrggtv9gg2ko963q foreign key (targetUser_id) references coursach.users
alter table coursach.user_has_course add constraint FK7ycpllljm7tfwb5iv9jaieew foreign key (courseid) references coursach.courses
alter table coursach.user_has_course add constraint FKlgsbp2k7f897kj6skeikw2ohc foreign key (id) references coursach.users
alter table coursach.user_roles_has_users add constraint FK940jtf09fainb5ymd07gqgiud foreign key (userId) references coursach.users
alter table coursach.courses_has_teachers add constraint FKan03ga59pwgeu924wp4ph1ax1 foreign key (userid) references coursach.users
alter table coursach.courses_has_teachers add constraint FKdx2ti8wu9pakdrf20wcj388if foreign key (coursesid) references coursach.courses
alter table coursach.lesson_has_tests add constraint FK3mjjy3givepogniivkk7sashs foreign key (testsid) references coursach.tests
alter table coursach.lesson_has_tests add constraint FKd5sdim5drdyhwh7phxkosac9g foreign key (lessonid) references coursach.lesson
alter table coursach.materials_has_content add constraint FK25d6utcobt44ku0yho80q5926 foreign key (contentid) references coursach.content
alter table coursach.materials_has_content add constraint FKqhr0ntk7suqislqju4bfn67cg foreign key (materialid) references coursach.materials
