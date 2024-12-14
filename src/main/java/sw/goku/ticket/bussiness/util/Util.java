package sw.goku.ticket.bussiness.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sw.goku.ticket.bussiness.controller.dto.TechnicalDto;
import sw.goku.ticket.bussiness.controller.dto.TicketResponse;
import sw.goku.ticket.bussiness.repository.entity.Ticket;
import sw.goku.ticket.security.repository.user.Users;

public class Util {
    // Supongamos que tienes un método para crear el ticket en el servicio.
    public static TicketResponse convertTicket(Ticket createdTicket) {
        // Convierte el Ticket creado a un TicketResponse
        String fullname = createdTicket.getUser().getFirstName() + " " + createdTicket.getUser().getLastName();
        return new TicketResponse(
                createdTicket.getTicketId(),
                createdTicket.getStatus(),
                createdTicket.getPriority(),
                createdTicket.getDescription(),
                createdTicket.getUser().getUsername(),
                fullname,
                createdTicket.getUser().getEmail(),
                convertOfUserToTechnicalDto(createdTicket.getTechnical()),
                createdTicket.getSubject(),
                createdTicket.getCreatedAt(),
                createdTicket.getAssignedAt(),
                createdTicket.getInProgressAt(),
                createdTicket.getResolvedAt()
        );
    }

    public static TechnicalDto convertOfUserToTechnicalDto(Users user) {
        if (user == null) {
            return new TechnicalDto(
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        String fullname = user.getFirstName() + " " + user.getLastName();
        return new TechnicalDto(
                user.getId(),
                user.getUsername(),
                fullname,
                user.getEmail(),
                user.getDni()
        );
    }


    public static String getUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();  // El nombre del usuario se almacena en el campo "name" de Authentication
        }
        return null;  // Si no hay autenticación, devuelve null
    }
}
