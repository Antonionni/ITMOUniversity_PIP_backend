package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
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
    private Timestamp createdAt;
    /**
     * date and time when test updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedAt;
    /**
     * List of questions
     */
    @OneToMany(mappedBy = "test")
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdat) {
        this.createdAt = createdat;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedat) {
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
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, threshold, createdAt, updatedAt);
    }

    public Collection<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<QuestionEntity> questionsById) {
        this.questions = questionsById;
    }
}
