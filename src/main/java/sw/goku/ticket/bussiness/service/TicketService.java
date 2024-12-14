package sw.goku.ticket.bussiness.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sw.goku.ticket.bussiness.repository.dao.AttachmentDao;
import sw.goku.ticket.bussiness.repository.dao.PriorityLevelDao;
import sw.goku.ticket.bussiness.repository.dao.TicketStatusDao;
import sw.goku.ticket.bussiness.repository.entity.Attachment;
import sw.goku.ticket.bussiness.repository.entity.Ticket;
import sw.goku.ticket.bussiness.repository.dao.TicketDao;
import sw.goku.ticket.bussiness.repository.entity.TicketStatus;
import sw.goku.ticket.bussiness.util.Util;
import sw.goku.ticket.security.jwt.JwtService;
import sw.goku.ticket.security.repository.UserRepository;
import sw.goku.ticket.security.repository.user.Users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketDao ticketDao;
    private final UserRepository userRepository;
    private final TicketStatusDao ticketStatusDao;
    private final PriorityLevelDao priorityLevelDao;
    private final AttachmentDao attachmentDao;
    private final EmailService emailService;


    @Value("${upload.path}")
    private String uploadDir;

    public Ticket createTicketWithAttachments(Ticket ticket, List<MultipartFile> files) {
        Users user = userRepository.findByUsername(Util.getUsernameFromContext())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Asocia el usuario al ticket
        ticket.setUser(user);
        ticket.setStatus(ticketStatusDao.findById(1).orElse(null));
        ticket.setPriority(priorityLevelDao.findById(ticket.getPriority().getPriorityId()).orElse(null));

        // Guarda el ticket
        Ticket savedTicket = ticketDao.save(ticket);

        // Maneja los archivos adjuntos
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // Genera un nombre único para el archivo
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                    // Guarda el archivo en la carpeta `uploads`
                    Path filePath = Paths.get(uploadDir, uniqueFileName);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, file.getBytes());

                    // Crea un nuevo objeto `Attachment`
                    Attachment attachment = new Attachment();
                    attachment.setName(uniqueFileName);
                    attachment.setTicket(savedTicket);

                    // Guarda el adjunto en la base de datos
                    attachmentDao.save(attachment);
                } catch (IOException e) {
                    throw new RuntimeException("Error al guardar el archivo: " + file.getOriginalFilename(), e);
                }
            }

            // Datos del template de email
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("fullName", savedTicket.getUser().getFirstName() + " " + savedTicket.getUser().getLastName());
            templateModel.put("ticketId", savedTicket.getTicketId());
            templateModel.put("subject", savedTicket.getSubject());
            templateModel.put("description", savedTicket.getDescription());

            try {
                emailService.sendTemplateEmail(
                        savedTicket.getUser().getEmail(),
                        "Nuevo ticket creado",
                        templateModel,
                        "createdTemplate"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return savedTicket;
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

    public List<Ticket> getTicketsWithNoTechnical() {
        return ticketDao.findTicketsWithNoTechnical();
    }

    public Ticket assignTicketToTechnician(int ticketId){
        Users user = userRepository.findByUsername(Util.getUsernameFromContext())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Ticket ticket = getTicketById(ticketId);

        if (validateTechnicianTicketLimit(user.getId())){
            throw new IllegalStateException("El técnico ha alcanzado el límite permitido de tickets asignados.");
        }

        ticket.setTechnical(user);
        ticket.setStatus(ticketStatusDao.findById(2).orElse(null));
        ticket.setAssignedAt(LocalDateTime.now());

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("fullName", ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName());
        templateModel.put("ticketId", ticket.getTicketId());
        templateModel.put("subject", ticket.getSubject());
        templateModel.put("description", ticket.getDescription());
        templateModel.put("technicianName", ticket.getTechnical().getFirstName() + " " + ticket.getTechnical().getLastName());

        try {
            emailService.sendTemplateEmail(
                    ticket.getUser().getEmail(),
                    "Técnico Asignado",
                    templateModel,
                    "assingnedTemplate"
            );
        } catch (Exception e) {
            e.printStackTrace(); // Maneja errores de envío de correo
        }

        return ticketDao.save(ticket);
    }

    public boolean validateTechnicianTicketLimit(int technicianId){
        return ticketDao.countTicketsByTechnicalId(technicianId) >= 6;
    }

    public List<Ticket> getTicketsByTechnical() {
        Users user = userRepository.findByUsername(Util.getUsernameFromContext())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return ticketDao.findAllTicketsByIdTechnical(user.getId());
    }

    public byte[] getAttachmentById(Long id) throws IOException {

        Attachment attachment = attachmentDao.findById(id).orElse(null);
        Path filePath = Paths.get(uploadDir, attachment.getName());

        return Files.readAllBytes(filePath);

    }

}
