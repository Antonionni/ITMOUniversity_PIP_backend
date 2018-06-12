package models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserHasCourseEntityPK implements Serializable {

    @Column(name = "courseid")
    private int courseId;
    /**
     * reference to {@link UserEntity}
     */

    @Column(name = "userid")
    private int userId;

    public int getCourseId() {
        return courseId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasCourseEntityPK that = (UserHasCourseEntityPK) o;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(courseId, userId);
    }
}
