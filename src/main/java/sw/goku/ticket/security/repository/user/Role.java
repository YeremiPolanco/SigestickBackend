package sw.goku.ticket.security.repository.user;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Role {
    ADMIN (Arrays.asList(Permission.ASSIGN_TICKETS, Permission.CANCEL_TICKETS, Permission.READ_ALL_TICKETS, Permission.CREATE_USERS, Permission.CREATE_TICKETS, Permission.GET_ROLE, Permission.READ_TICKETS)),
    USER (Arrays.asList(Permission.READ_PROBLEM, Permission.UPDATE_TICKETS, Permission.LIST_YOUR_TICKETS, Permission.CREATE_TICKETS, Permission.GET_ROLE, Permission.READ_TICKETS)),
    TECHNICAL (Arrays.asList(Permission.ASSIGN_TICKETS, Permission.CREATE_TICKETS, Permission.TICKETS_STATUS, Permission.GET_ROLE, Permission.READ_TICKETS_WITHOUT_A_TECHNICAL, Permission.READ_TICKETS, Permission.READ_TICKETS_TECHNICAL));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
