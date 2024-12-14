package sw.goku.ticket.bussiness.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sw.goku.ticket.bussiness.repository.entity.Ticket;

import java.util.List;

public interface TicketDao extends JpaRepository<Ticket, Integer> {

    @Query(value = "SELECT * FROM ticket WHERE user_id = :userId", nativeQuery = true)
    List<Ticket> findAllTicketsByIdUser(@Param("userId") int userId);

    // Nueva consulta para obtener los tickets donde technical_id es NULL
    @Query(value = "SELECT * FROM ticket WHERE technical_id IS NULL", nativeQuery = true)
    List<Ticket> findTicketsWithNoTechnical();

    @Query(value = "SELECT COUNT(*) FROM ticket WHERE technical_id = :technicalId AND status_id != 6", nativeQuery = true)
    int countTicketsByTechnicalId(@Param("technicalId") int technicalId);

    @Query(value = "SELECT * FROM ticket WHERE technical_id = :technicalId", nativeQuery = true)
    List<Ticket> findAllTicketsByIdTechnical(@Param("technicalId") int technicalId);
}
