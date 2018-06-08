package models.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class UserHasCourseEntityPK implements Serializable {
    private int id;
    /**
     * reference to {@link CourseEntity}
     */
    private int courseid;

    public int getId() {
        return id;
    }

    public int getCourseid() {
        return courseid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasCourseEntityPK that = (UserHasCourseEntityPK) o;
        return id == that.id &&
                courseid == that.courseid;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, courseid);
    }
}
