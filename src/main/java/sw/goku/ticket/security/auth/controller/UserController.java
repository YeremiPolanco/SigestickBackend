package sw.goku.ticket.security.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sw.goku.ticket.security.repository.user.Role;
import sw.goku.ticket.security.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Role> getRoleByUsername() {
        System.out.println("getRoleByUsername");
        try {
            return ResponseEntity.ok(userService.getRoleByUsername());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
