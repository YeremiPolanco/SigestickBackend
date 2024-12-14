package sw.goku.ticket.bussiness.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sw.goku.ticket.bussiness.repository.entity.Attachment;

public interface AttachmentDao extends JpaRepository<Attachment, Long> {
}
