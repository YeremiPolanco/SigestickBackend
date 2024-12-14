package sw.goku.ticket.bussiness.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import sw.goku.ticket.security.repository.user.Users;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Fecha de creación (se establece automáticamente)

    private LocalDateTime assignedAt; // Fecha de resolución del ticket

    private LocalDateTime inProgressAt; // Fecha en que el ticket pasa a estado "En Progreso"

    private LocalDateTime resolvedAt; // Fecha de resolución del ticket

    private String subject; // Campo para el asunto del ticket


    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket")
    @ToString.Exclude
    private List<TicketHistory> history;
}
