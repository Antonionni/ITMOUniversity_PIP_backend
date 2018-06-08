package models.entities;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import services.IUserDAO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users", catalog = "postgres")
public class UserEntity implements Subject {
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
    private int id;
    /**
     * user Email
     */
    private String email;
//    /**
//     * secret password
//     */
//    private String password;
    /**
     * Name
     */
    private String firstname;
    /**
     * Family Name
     */
    private String secondname;
    /**
     * date and time when user was created
     */
    private Timestamp createdat;
    /**
     * date and time when user was update his profile
     */
    private Timestamp updatedat;
    /**
     * List of courses which student try to pass
     */
    private Collection<CourseEntity> teacherCourses;

    private Collection<UserRolesHasUsersEntity> userRoles;

    private Collection<LinkedAccount> linkedAccounts;

    private boolean emailValidated;

    private boolean active;

    private Date lastLogin;

    private String name;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 400)
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

    @Basic
    @Column(name = "firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "secondname")
    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
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

    @ManyToMany(mappedBy = "courseTeachers")
    public Collection<CourseEntity> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(Collection<CourseEntity> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    @OneToMany(mappedBy = "user")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Collection<LinkedAccount> getLinkedAccounts() {
        return linkedAccounts;
    }

    public void setLinkedAccounts(Collection<LinkedAccount> linkedAccounts) {
        this.linkedAccounts = linkedAccounts;
    }

    @Column(name = "emailValidated")
    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    @Column(name = "isActive")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Column(name = "lastLogin")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Column(name = "name")
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
