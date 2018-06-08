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
     * id of {@link TestEntity}
     */
    @Basic
    @Column(name = "testid", nullable = false)
    private int testid;
    /**
     * count of right answers
     */
    @Basic
    @Column(name = "rightanswerid", nullable = false)
    private int rightanswerid;
    /**
     * List of {@link AnswerEntity} which existing at this question
     */
    @OneToMany(mappedBy = "questionsByQuestionid")
    private Collection<AnswerEntity> answersById;

    /**
     * reference tot {@link TestEntity}
     */
    @ManyToOne
    @JoinColumn(name = "testid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TestEntity testsByTestid;
    /**
     * refernce to {@link AnswerEntity}
     */
    @ManyToOne
    @JoinColumn(name = "rightanswerid", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private AnswerEntity answersByRightanswerid;
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

    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    public int getRightanswerid() {
        return rightanswerid;
    }

    public void setRightanswerid(int rightanswerid) {
        this.rightanswerid = rightanswerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return id == that.id &&
                testid == that.testid &&
                rightanswerid == that.rightanswerid &&
                Objects.equals(textquestion, that.textquestion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, textquestion, testid, rightanswerid);
    }

    public Collection<AnswerEntity> getAnswersById() {
        return answersById;
    }

    public void setAnswersById(Collection<AnswerEntity> answersById) {
        this.answersById = answersById;
    }

    public TestEntity getTestsByTestid() {
        return testsByTestid;
    }

    public void setTestsByTestid(TestEntity testsByTestid) {
        this.testsByTestid = testsByTestid;
    }

    public AnswerEntity getAnswersByRightanswerid() {
        return answersByRightanswerid;
    }

    public void setAnswersByRightanswerid(AnswerEntity answersByRightanswerid) {
        this.answersByRightanswerid = answersByRightanswerid;
    }
}
