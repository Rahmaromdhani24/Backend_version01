package rahma.backend.gestionPDEK.DTO;

import lombok.*;
import rahma.backend.gestionPDEK.Entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int matricule;
    private String nom;
    private String prenom;
    private String email;

    // Méthode pour convertir un User en UserDTO
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getMatricule(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail()
        );
    }
}
