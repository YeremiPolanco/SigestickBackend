package sw.goku.ticket.security.repository;

import sw.goku.ticket.security.repository.user.Role;
import sw.goku.ticket.security.repository.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    @Query(value = "SELECT u.roles FROM users u WHERE u.username = ?1", nativeQuery = true)
    Role findRoleByUsername(String username);
}
