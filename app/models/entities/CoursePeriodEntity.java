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
    private int id;

    @ManyToOne
    private CourseEntity course;
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
        return id == that.id &&
                Objects.equals(course, that.course) &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, course, startdate, enddate);
    }
}
