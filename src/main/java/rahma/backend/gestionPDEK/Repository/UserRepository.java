package rahma.backend.gestionPDEK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.*;

@Repository

public interface UserRepository extends JpaRepository<User , Integer> {

  Optional<User> findByMatricule(int matricule) ; 
  Optional<User> findByEmail(String email);
  Optional<User> findByRole(Role role);

}