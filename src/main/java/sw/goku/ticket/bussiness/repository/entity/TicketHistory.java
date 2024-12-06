package sw.goku.ticket.bussiness.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sw.goku.ticket.security.repository.user.Users;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "oldStatusId", nullable = false)
    private TicketStatus oldStatus;

    private Date changeDate;

    @ManyToOne
    @JoinColumn(name = "modifiedByUserId", nullable = false)
    private Users modifiedBy;
}
