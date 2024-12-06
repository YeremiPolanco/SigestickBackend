package sw.goku.ticket.security.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw.goku.ticket.security.auth.dto.AuthResponse;
import sw.goku.ticket.security.auth.dto.LoginRequest;
import sw.goku.ticket.security.auth.dto.RegisterRequest;
import sw.goku.ticket.security.service.AuthService;
import jakarta.validation.Valid;


@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        System.out.println("Solicitud de inicio de sesión recibida: " + request);
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("Gdddddddddddddddd");
        System.out.println("Register request controller: " + request);
        return ResponseEntity.ok(authService.register(request));
    }
}
