package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
public class UserEntity {
    /**
     * use unique identificator
     */
    private int id;
    /**
     * user Email
     */
    private String email;
    /**
     * secret password
     */
    private String password;
    /**
     * Name
     */
    private String firstname;
    /**
     * Family Name
     */
    private String secondname;
    /**
     * date and time when user was created
     */
    private Timestamp createdat;
    /**
     * date and time when user was update his profile
     */
    private Timestamp updatedat;
    /**
     * List of courses which student try to pass
     */
    private Collection<CourseEntity> teacherCourses;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email", nullable = false, length = -1)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false, length = -1)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "firstname", nullable = false, length = -1)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "secondname", nullable = false, length = -1)
    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    @Basic
    @Column(name = "createdat", nullable = true)
    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    @Basic
    @Column(name = "updatedat", nullable = true)
    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(secondname, that.secondname) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, email, password, firstname, secondname, createdat, updatedat);
    }

    @ManyToMany(mappedBy = "courseTeachers")
    public Collection<CourseEntity> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(Collection<CourseEntity> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }
}
