package models.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PasssageHasAnswersPK implements Serializable {
    private Integer id;
    /**
     * id of {@link AnswerEntity}
     */
    private Integer answerid;

    public Integer getId() {
        return id;
    }

    public Integer getAnswerid() {
        return answerid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasssageHasAnswersPK that = (PasssageHasAnswersPK) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(answerid, that.answerid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answerid);
    }
}
