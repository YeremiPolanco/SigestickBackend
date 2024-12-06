package sw.goku.ticket.bussiness.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sw.goku.ticket.bussiness.repository.entity.TicketStatus;

@Repository
public interface TicketStatusDao extends JpaRepository<TicketStatus, Integer> {
}
