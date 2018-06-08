package models.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PassageEntityPK implements Serializable {
    private int id;
    private Timestamp startdate;

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // TODO  eto primarykey
    @Column(name = "startdate", nullable = false)
    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageEntityPK that = (PassageEntityPK) o;
        return id == that.id &&
                Objects.equals(startdate, that.startdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate);
    }
}
