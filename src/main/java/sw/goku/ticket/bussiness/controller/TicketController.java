package sw.goku.ticket.bussiness.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sw.goku.ticket.bussiness.controller.dto.TicketResponse;
import sw.goku.ticket.bussiness.repository.entity.Ticket;
import sw.goku.ticket.bussiness.service.TicketService;
import sw.goku.ticket.bussiness.util.Util;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Ajustar según sea necesario
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TicketResponse> createTicket(
            @RequestPart("ticket") @Valid Ticket ticket,
            @RequestPart("attachments") List<MultipartFile> files) {

        System.out.println("Ticket recibido: " + ticket);
        Ticket createdTicket = ticketService.createTicketWithAttachments(ticket, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(Util.convertTicket(createdTicket));
    }


    @PostMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> assignTicketToTechnician(@PathVariable int ticketId) {
        Ticket assignedTicket = ticketService.assignTicketToTechnician(ticketId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Util.convertTicket(assignedTicket));
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

    @GetMapping("/tickets-with-no-technical")
    public ResponseEntity<List<TicketResponse>> getTicketsWithNoTechnical() {
        try {
            System.out.println("Controlador alcanzado");
            // Lógica de servicio
            return ResponseEntity.ok(ticketService.getTicketsWithNoTechnical()
                    .stream()
                    .map(Util::convertTicket)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            System.out.println("Error en el controlador: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }



    // Obtener un ticket por ID
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable int ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(Util.convertTicket(ticket));
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

    @GetMapping("/all-my-technical-ticket")
    public ResponseEntity<List<TicketResponse>> getTicketsByTechnical (){
        return ResponseEntity.ok(ticketService.getTicketsByTechnical()
                .stream()
                .map(Util::convertTicket) // Convertimos cada ticket a TicketResponse
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/attachment/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAttachment(@PathVariable Long id) throws IOException {
        byte[] image = ticketService.getAttachmentById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


}
