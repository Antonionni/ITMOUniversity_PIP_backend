package models.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
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
    private Date createdat;
    /**
     * date when answer was updated
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Date updatedat;
    /**
     * instans of {@link QuestionEntity}
     */
    @ManyToOne
    @JoinColumn(name = "questionid", referencedColumnName = "id", nullable = false)
    private QuestionEntity question;

    @Column(name = "rightanswer", nullable = false)
    private boolean isRight = false;

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

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerEntity that = (AnswerEntity) o;
        return id == that.id &&
                isRight == that.isRight &&
                Objects.equals(useranswer, that.useranswer) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat) &&
                Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, useranswer, createdat, updatedat, question, isRight);
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity questionsByQuestionid) {
        this.question = questionsByQuestionid;
    }
}
