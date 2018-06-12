package enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import config.RolesConst;

public enum RoleType {

    Student(RolesConst.Student), Teacher(RolesConst.Teacher), ApprovedTeacher(RolesConst.ApprovedTeacher), Admin(RolesConst.Admin), AuthenticatedUser(RolesConst.AuthenticatedUser);

    private String roleName;

    @JsonCreator
    public static RoleType forValue(String value) {
        return RoleType.valueOf(value);
    }

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
