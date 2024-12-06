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
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private Role role;
    private String username;

}
