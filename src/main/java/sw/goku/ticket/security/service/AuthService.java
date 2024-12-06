package sw.goku.ticket.security.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sw.goku.ticket.security.auth.dto.AuthResponse;
import sw.goku.ticket.security.auth.dto.LoginRequest;
import sw.goku.ticket.security.auth.dto.RegisterRequest;
import sw.goku.ticket.security.jwt.JwtService;
import sw.goku.ticket.security.repository.UserRepository;
import sw.goku.ticket.security.repository.user.Role;
import sw.goku.ticket.security.repository.user.Users;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetail = userRepository.findByUsername(request.getUsername()).orElseThrow();
        Users user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        Role role = user.getRole();
        String token = jwtService.getToken(userDetail);
        return AuthResponse.builder()
                .token(token)
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(role)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Users user = Users.builder()
                .username(usernameCreation(request.getFirstName(), request.getLastName(), request.getDni()))
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.valueOf(request.getRole().name()))
                .dni(request.getDni())
                .email(request.getEmail())
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }


    public String usernameCreation(String firstName, String lastName, String dni){
        return firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase() + dni;
    }
}
