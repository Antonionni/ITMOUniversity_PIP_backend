package models;

import enumerations.AnswerType;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "questions", schema = "public", catalog = "postgres")
public class QuestionEntity {
    /**
     * unique identificator
     */
    private int id;
    /**
     * text of question
     */
    private String textquestion;
    /**
     * id of {@link TestEntity}
     */
    private int testid;
    /**
     * count of right answers
     */
    private int rightanswerid;
    /**
     * List of {@link AnswerEntity} which existing at this question
     */
    private Collection<AnswerEntity> answersById;

    /**
     * reference tot {@link TestEntity}
     */
    private TestEntity testsByTestid;
    /**
     * refernce to {@link AnswerEntity}
     */
    private AnswerEntity answersByRightanswerid;
    /**
     * Enum of answer types
     */
    private AnswerType answerType;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated
    @Column(name = "answertype", nullable = false)
    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    @Basic
    @Column(name = "textquestion", nullable = false, length = -1)
    public String getTextquestion() {
        return textquestion;
    }

    public void setTextquestion(String textquestion) {
        this.textquestion = textquestion;
    }

    @Basic
    @Column(name = "testid", nullable = false)
    public int getTestid() {
        return testid;
    }

    public void setTestid(int testid) {
        this.testid = testid;
    }

    @Basic
    @Column(name = "rightanswerid", nullable = false)
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

    @OneToMany(mappedBy = "questionsByQuestionid")
    public Collection<AnswerEntity> getAnswersById() {
        return answersById;
    }

    public void setAnswersById(Collection<AnswerEntity> answersById) {
        this.answersById = answersById;
    }

    @ManyToOne
    @JoinColumn(name = "testid", referencedColumnName = "id", nullable = false)
    public TestEntity getTestsByTestid() {
        return testsByTestid;
    }

    public void setTestsByTestid(TestEntity testsByTestid) {
        this.testsByTestid = testsByTestid;
    }

    @ManyToOne
    @JoinColumn(name = "rightanswerid", referencedColumnName = "id", nullable = false)
    public AnswerEntity getAnswersByRightanswerid() {
        return answersByRightanswerid;
    }

    public void setAnswersByRightanswerid(AnswerEntity answersByRightanswerid) {
        this.answersByRightanswerid = answersByRightanswerid;
    }
}
