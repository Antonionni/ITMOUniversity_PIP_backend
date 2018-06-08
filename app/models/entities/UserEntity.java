package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import services.IUserDAO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users", catalog = "postgres", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserEntity implements Subject {
    @Transient
    public final IUserDAO IUserDAO;

    public UserEntity(IUserDAO IUserDAO) {
        this.IUserDAO = IUserDAO;
    }

    public UserEntity() {
        IUserDAO = null;
    }
    /**
     * use unique identificator
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    /**
     * user Email
     */
    @Basic
    @Column(name = "email", nullable = false, length = 400)
    private String email;
//    /**
//     * secret password
//     */
//    private String password;
    /**
     * Name
     */
    @Basic
    @Column(name = "firstname")
    private String firstname;
    /**
     * Family Name
     */
    @Basic
    @Column(name = "secondname")
    private String secondname;
    /**
     * date and time when user was created
     */
    @Basic
    @Column(name = "createdat", nullable = true)
    private Timestamp createdat;
    /**
     * date and time when user was update his profile
     */
    @Basic
    @Column(name = "updatedat", nullable = true)
    private Timestamp updatedat;
    /**
     * List of courses which student try to pass
     */
    @ManyToMany(mappedBy = "courseTeachers")
    private Collection<CourseEntity> teacherCourses;

    @OneToMany(mappedBy = "user")
    private Collection<UserRolesHasUsersEntity> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<LinkedAccount> linkedAccounts;

    @Column(name = "emailValidated")
    private boolean emailValidated;

    @Column(name = "isActive")
    private boolean active;

    @Column(name = "lastLogin")
    private Date lastLogin;

    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//
//    @Basic
//    @Column(name = "password", nullable = false)
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
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

    public Collection<CourseEntity> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(Collection<CourseEntity> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    public Collection<UserRolesHasUsersEntity> getUserRoles() {
        return userRoles;
    }

    @Override
    @Transient
    public List<? extends Role> getRoles() {
        return new ArrayList<>(userRoles);
    }

    @Override
    @Transient
    public List<? extends Permission> getPermissions() {
        return new ArrayList<>();
    }

    @Override
    @Transient
    public String getIdentifier() {
        return Integer.toString(id);
    }

    public void setUserRoles(Collection<UserRolesHasUsersEntity> userRoles) {
        this.userRoles = userRoles;
    }

    public Collection<LinkedAccount> getLinkedAccounts() {
        return linkedAccounts;
    }

    public void setLinkedAccounts(Collection<LinkedAccount> linkedAccounts) {
        this.linkedAccounts = linkedAccounts;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email) &&
//                Objects.equals(password, that.password) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(secondname, that.secondname) &&
                Objects.equals(createdat, that.createdat) &&
                Objects.equals(updatedat, that.updatedat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, /*password,*/ firstname, secondname, createdat, updatedat);
    }


}
