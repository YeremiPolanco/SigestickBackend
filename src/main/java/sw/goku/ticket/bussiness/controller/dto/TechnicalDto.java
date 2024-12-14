package sw.goku.ticket.bussiness.controller.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalDto {
    private Integer id;

    private String username;

    private String fullName;

    private String email;

    private String dni;
}
