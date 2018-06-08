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
    private Timestamp createdat;
    /**
     * date and time when test updated
     */
    @Basic
    @Column(name = "updateat", nullable = true)
    private Timestamp updatedat;
    /**
     * List of questions
     */
    @OneToMany(mappedBy = "testsByTestid")
    private Collection<QuestionEntity> questionsById;

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

    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

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
        TestEntity that = (TestEntity) o;
        return id == that.id &&
                threshold == that.threshold &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, threshold, createdat, updatedat);
    }

    public Collection<QuestionEntity> getQuestionsById() {
        return questionsById;
    }

    public void setQuestionsById(Collection<QuestionEntity> questionsById) {
        this.questionsById = questionsById;
    }
}
