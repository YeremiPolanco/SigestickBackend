package sw.goku.ticket.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sw.goku.ticket.bussiness.util.Util;
import sw.goku.ticket.security.exception.UserNotFoundException;
import sw.goku.ticket.security.repository.UserRepository;
import sw.goku.ticket.security.repository.user.Role;
import sw.goku.ticket.security.repository.user.Users;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Role getRoleByUsername() {
        String username = Util.getUsernameFromContext();
        return userRepository.findByUsername(username)
                .map(Users::getRole)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));
    }
}
