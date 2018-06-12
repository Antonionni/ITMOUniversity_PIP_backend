package models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PassageHasAnswersPK implements Serializable {
    private Integer id;

    public Integer getId() {
        return id;
    }

    private Integer answerId;

    public Integer getAnswerId() {
        return answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageHasAnswersPK that = (PassageHasAnswersPK) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, answerId);
    }
}
