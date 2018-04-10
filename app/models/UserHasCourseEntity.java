package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_has_course", schema = "public", catalog = "postgres")
public class UserHasCourseEntity {
    /**
     * date and time when this row was created
     */
    private Timestamp createdat;
    /**
     * date and time when this row was updated
     */
    private Timestamp updatedat;
    /**
     * id of {@link CourseEntity}
     */
    private int courseid;
    /**
     * unique identificator
     */
    private int id;
    /**
     * reference to {@link CourseEntity}
     */
    private CourseEntity coursesByCourseid;
    /**
     * reference to {@link UserEntity}
     */
    private UserEntity usersById;

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

    @Basic
    @Column(name = "courseid", nullable = false)
    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    @Basic
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasCourseEntity that = (UserHasCourseEntity) o;
        return courseid == that.courseid &&
                id == that.id &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(createdat, updatedat, courseid, id);
    }

    @ManyToOne
    @JoinColumn(name = "courseid", referencedColumnName = "id", nullable = false)
    public CourseEntity getCoursesByCourseid() {
        return coursesByCourseid;
    }

    public void setCoursesByCourseid(CourseEntity coursesByCourseid) {
        this.coursesByCourseid = coursesByCourseid;
    }

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    public UserEntity getUsersById() {
        return usersById;
    }

    public void setUsersById(UserEntity usersById) {
        this.usersById = usersById;
    }
}
