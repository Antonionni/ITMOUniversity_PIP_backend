package enumerations;

import config.RolesConst;

public enum RoleType {

    Student(RolesConst.Student), Teacher(RolesConst.Teacher), Admin(RolesConst.Admin), AuthenticatedUser(RolesConst.AuthenticatedUser);

    private String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
