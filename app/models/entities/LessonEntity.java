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
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * lesson title
     */
    @Basic
    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR")
    private String title;
    /**
     * reference of {@link CourseEntity}
     */
    @ManyToOne
    @JoinColumn(name = "coursesid", referencedColumnName = "id", nullable = false)
    private CourseEntity courses;
    /**
     * List of materials which used in this course
     */
    @OneToMany(mappedBy = "lessons")
    private Collection<MaterialEntity> materials;
    /**
     * List of test which used in this course
     */
    @ManyToMany
    @JoinTable(name = "lesson_has_tests",
            joinColumns = @JoinColumn(name = "lessonid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "testsid", referencedColumnName = "id"))
    private Collection<TestEntity> lessonTests;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonEntity that = (LessonEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title);
    }

    public CourseEntity getCourses() {
        return courses;
    }

    public void setCourses(CourseEntity coursesByCoursesid) {
        this.courses = coursesByCoursesid;
    }

    public Collection<MaterialEntity> getMaterials() {
        return materials;
    }

    public void setMaterials(Collection<MaterialEntity> materialsById) {
        this.materials = materialsById;
    }

    public Collection<TestEntity> getLessonTests() {
        return lessonTests;
    }

    public void setLessonTests(Collection<TestEntity> lessonTests) {
        this.lessonTests = lessonTests;
    }
}
