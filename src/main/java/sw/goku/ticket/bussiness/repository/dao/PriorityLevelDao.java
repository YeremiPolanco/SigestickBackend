package sw.goku.ticket.bussiness.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.goku.ticket.bussiness.repository.entity.PriorityLevel;

public interface PriorityLevelDao extends JpaRepository<PriorityLevel, Long> {
}
