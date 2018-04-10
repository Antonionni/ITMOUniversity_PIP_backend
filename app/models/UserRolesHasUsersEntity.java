package models;

import enumerations.RoleType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_roles_has_users", schema = "public", catalog = "postgres")
public class UserRolesHasUsersEntity {
    /**
     * unique identifiator
     */
    private int id;
    /**
     * date and time when user get his role
     */
    private Timestamp startdate;
    /**
     * date and time when user lose his role
     */
    private Timestamp enddate;
    /**
     * Enum of roles
     */
    private RoleType roleType;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated
    @Column(name = "role", nullable = false)
    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Basic
    @Column(name = "startdate", nullable = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolesHasUsersEntity that = (UserRolesHasUsersEntity) o;
        return id == that.id &&
                Objects.equals(startdate, that.startdate) &&
                Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, startdate, enddate);
    }
}
