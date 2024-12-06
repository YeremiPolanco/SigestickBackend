package sw.goku.ticket.security.repository.user;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN (Arrays.asList(Permission.ASSIGN_TICKETS, Permission.CANCEL_TICKETS, Permission.READ_ALL_TICKETS, Permission.CREATE_USERS, Permission.CREATE_TICKETS, Permission.GET_ROLE)),
    USER (Arrays.asList(Permission.READ_PROBLEM, Permission.UPDATE_TICKETS, Permission.LIST_YOUR_TICKETS, Permission.CREATE_TICKETS, Permission.GET_ROLE)),
    TECHNICAL (Arrays.asList(Permission.CREATE_TICKETS, Permission.TICKETS_STATUS, Permission.GET_ROLE));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
