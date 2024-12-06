package sw.goku.ticket.bussiness.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw.goku.ticket.bussiness.controller.dto.TicketResponse;
import sw.goku.ticket.bussiness.repository.entity.Ticket;
import sw.goku.ticket.bussiness.service.EmailService;
import sw.goku.ticket.bussiness.service.TicketService;
import sw.goku.ticket.bussiness.util.Util;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Ajustar según sea necesario
public class TicketController {

    private final TicketService ticketService;
    private final EmailService emailService;

    // Crear un ticket, el usuario se obtiene del token JWT
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody Ticket ticket, @RequestHeader("Authorization") String authHeader) {
        System.out.println("ticket: " + ticket);
        // Extraer el token JWT del header Authorization
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        // Llamamos al servicio para crear el ticket
        Ticket createdTicket = ticketService.createTicket(ticket, token);

        // Datos para el template del email
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("fullName", createdTicket.getUser().getFirstName() + " " + createdTicket.getUser().getLastName());
        templateModel.put("ticketId", createdTicket.getTicketId());
        templateModel.put("description", createdTicket.getDescription());

        try {
            emailService.sendTemplateEmail(
                    createdTicket.getUser().getEmail(),
                    "Nuevo ticket creado",
                    templateModel
            );
        } catch (Exception e) {
            e.printStackTrace(); // Maneja errores de envío de correo
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Util.convertTicket(createdTicket));
    }

    @GetMapping("/all-ticket")
    public ResponseEntity<List<TicketResponse>> getAllTicket (){
        return ResponseEntity.ok(ticketService.getAllTickets()
                .stream()
                .map(Util::convertTicket) // Convertimos cada ticket a TicketResponse
                .collect(Collectors.toList()));
    }

    @GetMapping("/all-my-ticket")
    public ResponseEntity<List<TicketResponse>> getAllMyTicket (){
        return ResponseEntity.ok(ticketService.getAllMyTickets()
                .stream()
                .map(Util::convertTicket) // Convertimos cada ticket a TicketResponse
                .collect(Collectors.toList()));
    }

    // Obtener un ticket por ID
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable int ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(ticket);
    }

    // Actualizar un ticket por ID
    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable int ticketId, @Valid @RequestBody Ticket ticketDetails) {
        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticketDetails);
        return ResponseEntity.ok(updatedTicket);
    }

    // Eliminar un ticket por ID
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

}
