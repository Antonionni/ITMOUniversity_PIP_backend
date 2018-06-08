package models.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "lesson", catalog = "postgres")
public class LessonEntity {
    /**
     * unique identificator
     */
    private int id;
    /**
     * reference to {@link CourseEntity}
     */
    private int coursesid;
    /**
     * lesson title
     */
    private String title;
    /**
     * reference of {@link CourseEntity}
     */
    private CourseEntity coursesByCoursesid;
    /**
     * List of materials which used in this course
     */
    //private Collection<MaterialEntity> materialsById;
    /**
     * List of test which used in this course
     */
    private Collection<TestEntity> lessonTests;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "coursesid", nullable = false)
    public int getCoursesid() {
        return coursesid;
    }

    public void setCoursesid(int coursesid) {
        this.coursesid = coursesid;
    }

    @Basic
    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonEntity that = (LessonEntity) o;
        return id == that.id &&
                coursesid == that.coursesid &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, coursesid, title);
    }

    @ManyToOne
    @JoinColumn(name = "coursesid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public CourseEntity getCoursesByCoursesid() {
        return coursesByCoursesid;
    }

    public void setCoursesByCoursesid(CourseEntity coursesByCoursesid) {
        this.coursesByCoursesid = coursesByCoursesid;
    }

  /*  @OneToMany(mappedBy = "lessonByLessonid")
    public Collection<MaterialEntity> getMaterialsById() {
        return materialsById;
    }

    public void setMaterialsById(Collection<MaterialEntity> materialsById) {
        this.materialsById = materialsById;
    }*/

    @ManyToMany
    @JoinTable(name = "lesson_has_tests",
    joinColumns = @JoinColumn(name = "lessonid", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "testsid", referencedColumnName = "id"))
    public Collection<TestEntity> getLessonTests() {
        return lessonTests;
    }

    public void setLessonTests(Collection<TestEntity> lessonTests) {
        this.lessonTests = lessonTests;
    }
}
