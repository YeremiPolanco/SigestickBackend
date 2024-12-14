package sw.goku.ticket.bussiness.controller.dto;

import lombok.*;
import sw.goku.ticket.bussiness.repository.entity.PriorityLevel;
import sw.goku.ticket.bussiness.repository.entity.TicketStatus;

import java.time.LocalDateTime;

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

    private TechnicalDto technical;

    private String subject;

    private LocalDateTime createdAt;

    private LocalDateTime assingnedAt;

    private LocalDateTime inProgressAt;

    private LocalDateTime resolvedAt;

}
