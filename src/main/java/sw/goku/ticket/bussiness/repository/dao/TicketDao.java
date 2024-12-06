package sw.goku.ticket.bussiness.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sw.goku.ticket.bussiness.repository.entity.Ticket;

import java.util.List;

public interface TicketDao extends JpaRepository<Ticket, Integer> {

    @Query(value = "SELECT * FROM ticket WHERE user_id = :userId", nativeQuery = true)
    List<Ticket> findAllTicketsByIdUser(@Param("userId") int userId);
}
