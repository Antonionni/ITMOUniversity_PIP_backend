package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "course_period", catalog = "postgres")
public class CoursePeriodEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "coursesid", nullable = false)
    private int coursesid;
    /**
     * date when course started
     */
    @Basic
    @Column(name = "startdate", nullable = true)
    private Timestamp startdate;
    /**
     * date when course was end
     */
    @Basic
    @Column(name = "enddate", nullable = true)
    private Timestamp enddate;

    public int getCoursesid() {
        return coursesid;
    }

    public void setCoursesid(int coursesid) {
        this.coursesid = coursesid;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

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
