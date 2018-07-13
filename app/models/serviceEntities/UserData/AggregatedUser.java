package models.serviceEntities.UserData;

import models.entities.UserEntity;

import java.util.Optional;

public class AggregatedUser {
    private BaseUser baseUser;
    private Teacher teacher;
    private Student student;
    private Admin admin;

    public AggregatedUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public AggregatedUser() {}

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public Optional<Teacher> getTeacher() {
        return Optional.ofNullable(teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Optional<Student> getStudent() {
        return Optional.ofNullable(student);
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Optional<Admin> getAdmin() {
        return Optional.ofNullable(admin);
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
