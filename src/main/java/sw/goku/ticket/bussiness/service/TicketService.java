package sw.goku.ticket.bussiness.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sw.goku.ticket.bussiness.repository.dao.PriorityLevelDao;
import sw.goku.ticket.bussiness.repository.dao.TicketStatusDao;
import sw.goku.ticket.bussiness.repository.entity.Ticket;
import sw.goku.ticket.bussiness.repository.dao.TicketDao;
import sw.goku.ticket.bussiness.repository.entity.TicketStatus;
import sw.goku.ticket.bussiness.util.Util;
import sw.goku.ticket.security.jwt.JwtService;
import sw.goku.ticket.security.repository.UserRepository;
import sw.goku.ticket.security.repository.user.Users;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketDao ticketDao;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TicketStatusDao ticketStatusDao;
    private final PriorityLevelDao priorityLevelDao;

    // Método para obtener el nombre de usuario desde el token
    private String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token); // Usa el JwtService para extraer el nombre de usuario
    }

    // Crear un ticket, asignando automáticamente el usuario autenticado
    @Transactional
    public Ticket createTicket(Ticket ticket, String token) {
        // Obtiene el nombre de usuario desde el token
        String username = getUsernameFromToken(token);

        // Busca el usuario por nombre de usuario
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Asocia el usuario al ticket
        ticket.setUser(user);
        ticket.setStatus(ticketStatusDao.findById(1).orElse(null));

        ticket.setPriority(priorityLevelDao.findById(ticket.getPriority().getPriorityId()).orElse(null));
        return ticketDao.save(ticket);
    }

    // Obtener ticket por ID
    public Ticket getTicketById(int ticketId) {
        return ticketDao.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado con ID: " + ticketId));
    }

    // Actualizar ticket
    @Transactional
    public Ticket updateTicket(int ticketId, Ticket ticketDetails) {
        Ticket existingTicket = getTicketById(ticketId);
        existingTicket.setDescription(ticketDetails.getDescription());
        existingTicket.setStatus(ticketDetails.getStatus());
        existingTicket.setPriority(ticketDetails.getPriority());
        existingTicket.setUser(ticketDetails.getUser());
        return ticketDao.save(existingTicket);
    }

    // Eliminar ticket
    @Transactional
    public void deleteTicket(int ticketId) {
        Ticket ticket = getTicketById(ticketId);
        ticketDao.delete(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDao.findAll();
    }

    public List<Ticket> getAllMyTickets() {
        Users user = userRepository.findByUsername(Util.getUsernameFromContext())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return ticketDao.findAllTicketsByIdUser(user.getId());
    }
}
