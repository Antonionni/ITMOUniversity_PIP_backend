package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "courses", schema = "public", catalog = "postgres")
public class CourseEntity {
    private int id;
    private String title;
    private String subject;
    private String imageurl;
    private Timestamp createdat;
    private Timestamp updatedat;
    private Collection<CoursePeriodEntity> coursePeriodsById;
    private Collection<LessonEntity> lessonsById;
    private Collection<UserEntity> courseTeachers;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "subject", nullable = false, length = -1)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "imageurl", nullable = false, length = -1)
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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
        CourseEntity that = (CourseEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(imageurl, that.imageurl) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, subject, imageurl, createdat, updatedat);
    }

    @OneToMany(mappedBy = "coursesByCoursesid")
    public Collection<CoursePeriodEntity> getCoursePeriodsById() {
        return coursePeriodsById;
    }

    public void setCoursePeriodsById(Collection<CoursePeriodEntity> coursePeriodsById) {
        this.coursePeriodsById = coursePeriodsById;
    }

    @OneToMany(mappedBy = "coursesByCoursesid")
    public Collection<LessonEntity> getLessonsById() {
        return lessonsById;
    }

    public void setLessonsById(Collection<LessonEntity> lessonsById) {
        this.lessonsById = lessonsById;
    }

    @ManyToMany
    @JoinTable(
            name="courses_has_teachers",
            joinColumns=@JoinColumn(name="coursesid", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="userid", referencedColumnName="id"))
    public Collection<UserEntity> getCourseTeachers() {
        return courseTeachers;
    }

    public void setCourseTeachers(Collection<UserEntity> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }
}
