package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tests", catalog = "postgres")
public class TestEntity {
    /**
     * test unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * title of test
     */
    @Basic
    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR")
    private String title;
    /**
     * minimal score to pass the test
     */
    @Basic
    @Column(name = "threshold", nullable = false)
    private int threshold;
    /**
     * date and time when test created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Date createdAt;
    /**
     * date and time when test updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "lessonid", referencedColumnName = "id", nullable = false)
    private LessonEntity lesson;

    public LessonEntity getLesson() {
        return lesson;
    }

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
    }

    /**
     * List of questions
     */
    @OneToMany(mappedBy = "test", orphanRemoval = true)
    private Collection<QuestionEntity> questions;

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

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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
        TestEntity that = (TestEntity) o;
        return id == that.id &&
                threshold == that.threshold &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(lesson, that.lesson) &&
                Objects.equals(questions, that.questions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, threshold, createdAt, updatedAt, lesson, questions);
    }

    public Collection<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<QuestionEntity> questionsById) {
        this.questions = questionsById;
    }
}
