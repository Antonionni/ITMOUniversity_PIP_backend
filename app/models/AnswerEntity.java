package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "answers", schema = "public", catalog = "postgres")
public class AnswerEntity {
    private int id;
    private String useranswer;
    private Timestamp createdat;
    private Timestamp updatedat;
    private int questionid;
    private QuestionEntity questionsByQuestionid;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "useranswer", nullable = false, length = -1)
    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
    }

    @Basic
    @Column(name = "createdat", nullable = true)
    public Timestamp getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Timestamp createdat) {
        this.createdat = createdat;
    }

    @Basic
    @Column(name = "updatedat", nullable = true)
    public Timestamp getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Timestamp updatedat) {
        this.updatedat = updatedat;
    }

    @Basic
    @Column(name = "questionid", nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "questionid", referencedColumnName = "id", nullable = false)
    public QuestionEntity getQuestionsByQuestionid() {
        return questionsByQuestionid;
    }

    public void setQuestionsByQuestionid(QuestionEntity questionsByQuestionid) {
        this.questionsByQuestionid = questionsByQuestionid;
    }
}
