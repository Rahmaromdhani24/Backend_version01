package rahma.backend.gestionPDEK.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operateur")
@CrossOrigin(origins = "http://localhost:4200") 
public class OperateurController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjetRepository projetRepository ;


    public OperateurController(UserRepository userRepository, RoleRepository roleRepository , ProjetRepository projetRepository  ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projetRepository = projetRepository ; 
    }
    @GetMapping("/getOperateur/{matricule}")
    public ResponseEntity<?> getUser(@PathVariable int matricule) {
        Optional<User> userOptional = userRepository.findById(matricule);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur introuvable !"));
        }

        User user = userOptional.get();

        // Vérifie si le rôle de l'utilisateur est bien OPERATEUR
        if (!user.getRole().getNom().equalsIgnoreCase("OPERATEUR")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Ce matricule ne représente pas un opérateur !"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("matricule", user.getMatricule());
        response.put("plant", user.getPlant());
        response.put("operation", user.getTypeOperation());
        response.put("nom", user.getNom());
        response.put("prenom", user.getPrenom());
        response.put("role", user.getRole().getNom());
        response.put("poste", user.getPoste());
        response.put("segment", user.getSegment());
        response.put("machine", user.getMachine());

        return ResponseEntity.ok(response);
    }
   @GetMapping("/projets/{plantName}")
public ResponseEntity<?> getProjetsByPlant(@PathVariable String plantName) {
    try {
        Plant plant = Plant.valueOf(plantName.toUpperCase());
        List<String> projetNames = projetRepository.findByPlant(plant).stream()
                .map(Projet::getNom)  // Map chaque projet à son nom
                .collect(Collectors.toList());  // Collecter dans une liste
        return ResponseEntity.ok(projetNames);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Plant invalide : " + plantName);
    }
}

}
