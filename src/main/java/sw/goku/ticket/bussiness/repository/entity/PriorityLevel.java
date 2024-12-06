package sw.goku.ticket.bussiness.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriorityLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priorityId;

    private String level;
}
