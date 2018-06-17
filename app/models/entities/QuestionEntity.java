package models.entities;

import enumerations.AnswerType;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "questions", catalog = "postgres")
public class QuestionEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * text of question
     */
    @Basic
    @Column(name = "textquestion", nullable = false, columnDefinition = "VARCHAR")
    private String textquestion;

    /**
     * List of {@link AnswerEntity} which existing at this question
     */
    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    @Column(nullable = false)
    private Collection<AnswerEntity> answers;

    /**
     * reference tot {@link TestEntity}
     */
    @ManyToOne
    @JoinColumn(name = "testid", referencedColumnName = "id", nullable = false)
    private TestEntity test;
    /**
     * Enum of answer types
     */
    @Enumerated
    @Column(name = "answertype", nullable = false)
    private AnswerType answerType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public String getTextquestion() {
        return textquestion;
    }

    public void setTextquestion(String textquestion) {
        this.textquestion = textquestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return id == that.id &&
                Objects.equals(textquestion, that.textquestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textquestion);
    }

    public Collection<AnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<AnswerEntity> answersById) {
        this.answers = answersById;
    }

    public TestEntity getTest() {
        return test;
    }

    public void setTest(TestEntity testsByTestid) {
        this.test = testsByTestid;
    }
}
