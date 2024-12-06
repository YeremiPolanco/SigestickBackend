package sw.goku.ticket.security.repository.user;

public enum Permission {
    //ADMIN
    READ_ALL_TICKETS,
    ASSIGN_TICKETS,
    CANCEL_TICKETS, //compartido con el técnico y usuario
    CREATE_USERS,
    //TECHNICIANS
    READ_PROBLEM,
    UPDATE_TICKETS,
    //USER
    CREATE_TICKETS,
    TICKETS_STATUS,
    LIST_YOUR_TICKETS,

    GET_ROLE
}
