package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.PistoletServiceImplimenetation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.Plant;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/operations/pistolet")
@RequiredArgsConstructor
public class PistoletController {

	 @Autowired  private  PistoletServiceImplimenetation pistoletService;

	 @PostMapping("/ajouterPDEK/{matricule}")
	 public ResponseEntity<Void> ajouterPDEK(@PathVariable int matricule, @RequestBody Pistolet pistolet) {
	     try {
	         pistoletService.ajouterPistolet(matricule, pistolet);
	         return ResponseEntity.ok().build(); // Retourne un 200 sans corps
	     } catch (Exception e) {
	         return ResponseEntity.badRequest().build(); // Retourne un 400 sans message
	     }
	 }



	 /*  @GetMapping("/dernier-numero-cycle")
	      public ResponseEntity<?> getLastNumeroCycle(
	              @RequestParam String sectionFilSelectionne,
	              @RequestParam int segment,
	              @RequestParam Plant nomPlant,
	              @RequestParam String projetName) {

	          Optional<Integer> dernierNumeroCycle = pistoletService.getLastNumeroCycle(sectionFilSelectionne, segment, nomPlant, projetName);

	          return dernierNumeroCycle
	                  .map(ResponseEntity::ok) // Si le dernier numéro de cycle est présent, renvoyer 200 OK avec la valeur
	                  .orElseGet(() -> ResponseEntity.noContent().build()); // Sinon, renvoyer 204 No Content
	      }
 */
    
}
