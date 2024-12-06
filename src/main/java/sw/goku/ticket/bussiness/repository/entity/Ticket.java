package sw.goku.ticket.bussiness.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sw.goku.ticket.security.repository.user.Users;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "statusId", nullable = false)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "priorityId", nullable = false)
    private PriorityLevel priority;

    @ManyToOne
    @JoinColumn(name = "technical_id")
    private Users technical;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    private List<Attachment> attachments;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    private List<TicketHistory> history;
}
