package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "answers", catalog = "postgres")
public class AnswerEntity {
    /**
     * unique identificator
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    /**
     * answer which user chose
     */
    @Basic
    @Column(name = "useranswer", nullable = false, columnDefinition = "VARCHAR")
    private String useranswer;
    /**
     * date when answer was created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Timestamp createdat;
    /**
     * date when answer was updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedat;
    /**
     * id of {@link QuestionEntity}
     */
    @Basic
    @Column(name = "questionid", nullable = false)
    private int questionid;
    /**
     * instans of {@link QuestionEntity}
     */
    @ManyToOne
    @JoinColumn(name = "questionid", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private QuestionEntity questionsByQuestionid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
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

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerEntity that = (AnswerEntity) o;
        return id == that.id &&
                questionid == that.questionid &&
                Objects.equals(useranswer, that.useranswer) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, useranswer, createdat, updatedat, questionid);
    }

    public QuestionEntity getQuestionsByQuestionid() {
        return questionsByQuestionid;
    }

    public void setQuestionsByQuestionid(QuestionEntity questionsByQuestionid) {
        this.questionsByQuestionid = questionsByQuestionid;
    }
}
