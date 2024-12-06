package sw.goku.ticket.security.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sw.goku.ticket.security.repository.user.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String firstName;
    private String email;
    private String lastName;
    private String dni;
    private Role role;
}
