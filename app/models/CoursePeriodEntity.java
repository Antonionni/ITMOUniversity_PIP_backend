package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "course_period", schema = "public", catalog = "postgres")
public class CoursePeriodEntity {
    private int coursesid;
    private Timestamp startdate;
    private Timestamp enddate;

    @Id
    @Column(name = "coursesid", nullable = false)
    public int getCoursesid() {
        return coursesid;
    }

    public void setCoursesid(int coursesid) {
        this.coursesid = coursesid;
    }

    @Basic
    @Column(name = "startdate", nullable = true)
    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    @Basic
    @Column(name = "enddate", nullable = true)
    public Timestamp getEnddate() {
        return enddate;
    }

    public void setEnddate(Timestamp enddate) {
        this.enddate = enddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursePeriodEntity that = (CoursePeriodEntity) o;
        return coursesid == that.coursesid &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(coursesid, startdate, enddate);
    }
}
