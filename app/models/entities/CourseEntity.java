package models.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "courses", catalog = "postgres")
public class CourseEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * title of course
     */
    @Basic
    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR")
    private String title;
    /**
     * education subject (for example Math, Philosophy e.t.c.)
     */
    @Basic
    @Column(name = "subject", nullable = false, columnDefinition = "VARCHAR")
    private String subject;
    /**
     * url for course image
     */
    @Basic
    @Column(name = "imageurl", nullable = false, columnDefinition = "VARCHAR")
    private String imageurl;
    /**
     * date when course was created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Date createdAt;
    /**
     * date when course was updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Date updatedAt;
    /**
     * List of coruses periods
     */
    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private Collection<CoursePeriodEntity> coursePeriods;
    /**
     * List of course lessons
     */
    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private Collection<LessonEntity> lessons;
    /**
     * List of theachers who created course
     */
    @ManyToMany
    @JoinTable(
            name="courses_has_teachers",
            joinColumns=@JoinColumn(name="coursesid", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="userid", referencedColumnName="id"))
    private Collection<UserEntity> courseTeachers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdat) {
        this.createdAt = createdat;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedat) {
        this.updatedAt = updatedat;
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
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, subject, imageurl, createdAt, updatedAt);
    }

    public Collection<CoursePeriodEntity> getCoursePeriods() {
        return coursePeriods;
    }

    public void setCoursePeriods(Collection<CoursePeriodEntity> coursePeriodsById) {
        this.coursePeriods = coursePeriodsById;
    }

    public Collection<LessonEntity> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<LessonEntity> lessonsById) {
        this.lessons = lessonsById;
    }

    public Collection<UserEntity> getCourseTeachers() {
        return courseTeachers;
    }

    public void setCourseTeachers(Collection<UserEntity> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }
}
