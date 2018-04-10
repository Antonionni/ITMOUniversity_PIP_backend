package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "passages", schema = "public", catalog = "postgres")
@IdClass(PassageEntityPK.class)
public class PassageEntity {
    /**
     * unique identificator
     */
    private int id;
    /**
     * date and time when student start pass the test
     */
    private Timestamp startdate;
    /**
     * date and time when student end pass the test
     */
    private Timestamp enddate;
    /**
     * reference of {@link TestEntity}
     */
    private int testid;
    /**
     * result score
     */
    private int result;
    /**
     * is student pass the test
     */
    private boolean isRight;
    /**
     * refernce to {@link UserEntity}
     */
    private UserEntity usersById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "startdate", nullable = false)
    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    @Basic
    @Column(name = "enddate", nullable = true)
    public Timestamp getEnddate() {
        return enddate;
    }

    public void setEnddate(Timestamp enddate) {
        this.enddate = enddate;
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
    @Column(name = "result", nullable = false)
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Basic
    @Column(name = "is_right", nullable = false)
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
        PassageEntity that = (PassageEntity) o;
        return id == that.id &&
                testid == that.testid &&
                result == that.result &&
                isRight == that.isRight &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, enddate, testid, result, isRight);
    }

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    public UserEntity getUsersById() {
        return usersById;
    }

    public void setUsersById(UserEntity usersById) {
        this.usersById = usersById;
    }
}
