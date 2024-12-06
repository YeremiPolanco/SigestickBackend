package sw.goku.ticket.bussiness.controller.dto;

import lombok.*;
import sw.goku.ticket.bussiness.repository.entity.PriorityLevel;
import sw.goku.ticket.bussiness.repository.entity.TicketStatus;
import sw.goku.ticket.security.repository.user.Users;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketResponse {
    private Long ticketId;

    private TicketStatus status;

    private PriorityLevel priority;

    private String description;

    private String username;

    private String fullName;

    private String email;

    private Users technical;

}
