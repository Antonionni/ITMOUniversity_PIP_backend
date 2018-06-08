package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_has_course", catalog = "postgres")
@IdClass(UserHasCourseEntityPK.class)
public class UserHasCourseEntity {
    /**
     * date and time when this row was created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Timestamp createdat;
    /**
     * date and time when this row was updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedat;

    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * id of {@link CourseEntity}
     */
    @Id
    @Column(name = "courseid", nullable = false)
    private int courseid;
    /**
     * reference to {@link CourseEntity}
     */
    @ManyToOne
    @JoinColumn(name = "courseid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private CourseEntity coursesByCourseid;
    /**
     * reference to {@link UserEntity}
     */
    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private UserEntity usersById;

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

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

    public CourseEntity getCoursesByCourseid() {
        return coursesByCourseid;
    }

    public void setCoursesByCourseid(CourseEntity coursesByCourseid) {
        this.coursesByCourseid = coursesByCourseid;
    }

    public UserEntity getUsersById() {
        return usersById;
    }

    public void setUsersById(UserEntity usersById) {
        this.usersById = usersById;
    }
}
