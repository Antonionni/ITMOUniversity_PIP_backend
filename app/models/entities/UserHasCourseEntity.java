package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_has_course", catalog = "postgres")
public class UserHasCourseEntity {
    /**
     * date and time when this row was created
     */
    @EmbeddedId
    private UserHasCourseEntityPK id;

    @Basic
    @Column(name = "createdat", nullable = true)
    private Timestamp createdAt;
    /**
     * date and time when this row was updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedAt;

    /**
     * reference to {@link CourseEntity}
     */
    @ManyToOne
    /*@JoinColumn(name = "courseid", referencedColumnName = "id", nullable = false)*/
    @MapsId("courseId")
    private CourseEntity course;
    /**
     * reference to {@link UserEntity}
     */
    @ManyToOne
    /*@JoinColumn(name = "id", referencedColumnName = "id", nullable = false)*/
    @MapsId("userId")
    private UserEntity user;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdat) {
        this.createdAt = createdat;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedat) {
        this.updatedAt = updatedat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasCourseEntity that = (UserHasCourseEntity) o;
        return id == that.id &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(createdAt, updatedAt, id);
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity coursesByCourseid) {
        this.course = coursesByCourseid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity usersById) {
        this.user = usersById;
    }
}
